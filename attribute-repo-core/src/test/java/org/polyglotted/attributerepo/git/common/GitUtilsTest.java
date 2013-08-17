package org.polyglotted.attributerepo.git.common;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Date;

import org.junit.Test;

import com.google.gson.Gson;

public class GitUtilsTest extends GitUtils {

    @Test
    public void testEncode() {
        assertEquals("unit%2Ftest", encode("unit/test"));
    }

    @Test
    public void testDecode() {
        assertEquals("unit/test", decode("unit%2Ftest"));
    }

    @Test
    public void testToBase64() {
        assertEquals("dW5pdHRlc3Q=", toBase64("unittest"));
    }

    @Test
    public void testFromBase64() {
        assertEquals("unittest", fromBase64("dW5pdHRlc3Q="));
    }

    @Test
    public void testAddParams() {
        assertEquals("", addParams(null));
        assertEquals("", addParams(Collections.<String, String>emptyMap()));
    }

    @Test
    public void testGsonDate() {
        Gson gson = createGson(true);
        assertEquals("\"2013-08-17T11:47:56Z\"", gson.toJson(new Date(1376740076000l)));
        assertEquals(1376740076000l, gson.fromJson("\"2013-08-17T11:47:56Z\"", Date.class).getTime());
    }
}
