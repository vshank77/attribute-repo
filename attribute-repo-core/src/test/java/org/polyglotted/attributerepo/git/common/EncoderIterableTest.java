package org.polyglotted.attributerepo.git.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class EncoderIterableTest {

    @Test
    public void testIterator() {
        Map<String, String> map = ImmutableMap.<String, String> builder().put("test/", "?value").build();
        Iterator<Entry<String, String>> iterator = new EncoderIterable(map).iterator();
        assertTrue(iterator.hasNext());
        Entry<String, String> next = iterator.next();
        assertEquals("test%2F", next.getKey());
        assertEquals("%3Fvalue", next.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        Map<String, String> map = ImmutableMap.<String, String> builder().put("test/", "?value").build();
        Iterator<Entry<String, String>> iterator = new EncoderIterable(map).iterator();
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorSetValue() {
        Map<String, String> map = ImmutableMap.<String, String> builder().put("test/", "?value").build();
        Iterator<Entry<String, String>> iterator = new EncoderIterable(map).iterator();
        iterator.next().setValue("newValue");
    }
}
