package org.no.generator.configuration;

import static org.no.generator.configuration.util.ObjectUtils.apply;
import static org.no.generator.configuration.util.ObjectUtils.safe;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.no.generator.Store;
import org.no.generator.configuration.context.DependencyContext;
import org.no.generator.configuration.util.ObjectUtils;

public class StoreHandlerLucene extends ConfigurationHandlerDefault<Store, StoreHandlerLucene.Configuration> {

    private Map<Integer, Reference<Directory>> directories;

    private StandardAnalyzer analyzer = new StandardAnalyzer();

    @Override
    protected Store create(Configuration c, DependencyContext context) {

        if (directories == null) {
            directories = new HashMap<>();
        }

        Store store = context.get(Store.class, c.store);
        if (c.query == null) {
            return store;
        }

        Object[] storeData = store.fetch();

        int directoryIdentity = System.identityHashCode(store);

        Directory directory;

        Reference<Directory> directoryReference = directories.get(directoryIdentity);
        if (directoryReference == null || (directory = directoryReference.get()) == null) {
            directories.put(directoryIdentity, new WeakReference<>(directory = createIndex(c, storeData)));
        }

        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] { "name" }, analyzer);
        Query query;
        try {
            query = queryParser.parse(c.query);
        } catch (ParseException e) {
            throw new ConfigurationException("Unable to parse query: " + c.query);
        }

        try (IndexReader ir = DirectoryReader.open(directory)) {
            IndexSearcher indexSearcher = new IndexSearcher(ir);

            TopDocs td = indexSearcher.search(query, Integer.MAX_VALUE);
            if (td.scoreDocs.length == 0) {
                throw new ConfigurationException("No rows matching query: " + c.query);
            }

            Object[] data = ObjectUtils.apply(scoreDoc -> {
                return storeData[getDocumentId(indexSearcher.doc(scoreDoc.doc))];
            }, td.scoreDocs);

            info(() -> getClass() + ": stored " + data.length + " item" + (data.length != 1 ? "s" : ""));

            return () -> {
                return data;
            };
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Directory createIndex(Configuration c, Object[] data) {
        Directory directory;
        switch (c.indexType) {
            case MEMORY:
                directory = new RAMDirectory();
                break;
            case SIMPLE:
                try {
                    Path indexPath = c.indexPath;
                    if (indexPath == null) {
                        indexPath = File.createTempFile("idx", null).toPath();
                    }
                    directory = new SimpleFSDirectory(indexPath);
                } catch (IOException e) {
                    throw new ConfigurationException("Unable to initialize index directory: " + c.indexPath);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported index type: " + c.indexType);
        }

        try (IndexWriter iw = new IndexWriter(directory, new IndexWriterConfig(analyzer))) {
            apply((index, dataItem) -> iw.addDocument(createDocument(index, dataItem)), data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return directory;
    }

    private static final String F_ID   = "_id";

    private Document createDocument(int id, Object data) {
        Document document = new Document();
        setDocumentId(document, id);
        Map<String, Object> map = safe(data);
        map.forEach((k, v) -> {
            if (v != null) {
                document.add(createSearchField(k, v));
            }
        });
        return document;
    }

    private Field createSearchField(String n, Object v) {
        return new TextField(n, v.toString(), Field.Store.NO);
    }

    private int getDocumentId(Document document) {
        return document.getField(F_ID).numericValue().intValue();
    }

    private int setDocumentId(Document document, int id) {
        document.add(new IntField(F_ID, id, Field.Store.YES));
        return id;
    }

    public static final class Configuration {

        private Object store;

        private String query;

        private Type indexType = Type.MEMORY;

        private Path indexPath;

        public enum Type {
            MEMORY,
            SIMPLE
        }

    }

}
