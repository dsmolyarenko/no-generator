#### Base Configuration

* **simple** - stores data provided explicitly by `names`, `types` and `data` sections. See the example below:
```yaml
main:
  type: join
  delimiter: |+ 
    ,
  sample:
    type: store
    store: test
stores:
  - id: test
    type: simple
    names:
      [ code, value ]
    data:
      - [ c1, c1_v1 ]
      - [ c1, c1_v2 ]
      - [ c1, c1_v3 ]
      - [ c1, c1_v4 ]
```

* **csv** - stores data derived from CSV source defined in `data` or file specified in `path`. Either `data` or `path` property should be defined. The text in the `data` section should be line based. File `path` can start from '/' that refers to a built-in resource hosted in the jar file, otherwise a specific protocol should be specified: file:/tmp/source.csv, http... See the general section for the full list of supported protocols. The CSV source should contain a header with column names e.g.: id, name, code, ... An access to store object is case insensitive. Default column type is 'string' but can be additionally specified in the header line via colon: id:int, name:string, code:string, location:geometry. All supported type conversions are specified in the general section. See the example below:
```yaml
main:
  type: join
  delimiter: |+ 
    ,
  sample:
    type: store
    store: test
stores:
  - id: test
    type: csv
    path: /db-demo1.csv
```