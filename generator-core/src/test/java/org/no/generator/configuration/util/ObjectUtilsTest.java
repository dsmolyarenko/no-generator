package org.no.generator.configuration.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ObjectUtilsTest {

    @Test
    public void mergeTest() {
        Map<String, Object> n1 = new LinkedHashMap<>();
        n1.put("v1", "v1");
        n1.put("v2", "v2");

        Map<String, Object> n2 = new LinkedHashMap<>();
        n2.put("v1", "v1");
        n2.put("v2", "v2");

        Map<String, Object> dst = new LinkedHashMap<>();
        dst.put("id", "i0");
        dst.put("c1", "c1");
        dst.put("c2", "c2");
        dst.put("n1", n1);
        dst.put("n2", n1);
        dst.put("l1", Arrays.asList("a1", "a2"));

        Map<String, Object> n1Override = new LinkedHashMap<>();
        n1Override.put("v1", "v1Override");

        Map<String, Object> updates = new LinkedHashMap<>();
        updates.put("id", "i1");
        updates.put("c1", "c1Override");
        updates.put("n1", n1Override);
        updates.put("l1", Arrays.asList("a1Override"));

        Map<String, Object> merged = ObjectUtils.merge(dst, updates);

        Assert.assertEquals("i1"        , merged.get("id"));
        Assert.assertEquals("c1Override", merged.get("c1"));
        Assert.assertEquals("c2"        , merged.get("c2"));
        Assert.assertEquals("v1Override", ((Map )merged.get("n1")).get("v1"));
        Assert.assertEquals("v2"        , ((Map )merged.get("n1")).get("v2"));
        Assert.assertEquals("v1"        , ((Map )merged.get("n2")).get("v1"));
        Assert.assertEquals("v2"        , ((Map )merged.get("n2")).get("v2"));
        Assert.assertEquals("a1Override", ((List)merged.get("l1")).get(0));

    }
}