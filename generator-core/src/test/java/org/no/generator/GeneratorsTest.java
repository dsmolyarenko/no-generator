package org.no.generator;

import org.junit.Assert;
import org.junit.Test;

public class GeneratorsTest {

    @Test
    public void createStringHexTest() {
        byte v1 = +127;
        byte v2 = -127;
        String result = Generators.toString(Generators.createStringHex(",", v1, v2));
        Assert.assertEquals("7f,81", result);
    }

    @Test
    public void createStringDecTest() {
        byte v1 = +127;
        byte v2 = -127;
        String result = Generators.toString(Generators.createStringDec(",", v1, v2));
        Assert.assertEquals("127,-127", result);
    }

    @Test
    public void createNetworkIp() {
        String result = Generators.toString(Generators.createNetworkIp());
        Assert.assertNotNull(result);
    }

    @Test
    public void createNetworkMac() {
        String result = Generators.toString(Generators.createNetworkMac());
        Assert.assertNotNull(result);
    }

    @Test
    public void createNetworkHost() {
        String result = Generators.toString(Generators.createNetworkHost());
        Assert.assertNotNull(result);
    }
}
