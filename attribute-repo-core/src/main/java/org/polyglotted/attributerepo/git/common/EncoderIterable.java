package org.polyglotted.attributerepo.git.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.Sets;

class EncoderIterable implements Iterable<Map.Entry<String, String>> {

    private final Set<Entry<String, String>> entrySet; 
    
    public EncoderIterable(Map<String, String> source) {
        entrySet = Sets.newHashSet(source.entrySet());
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        final Iterator<Entry<String, String>> entryIter = entrySet.iterator();
        return new Iterator<Entry<String, String>>() {
            @Override
            public boolean hasNext() {
                return entryIter.hasNext();
            }

            @Override
            public Entry<String, String> next() {
                final Entry<String, String> entry = entryIter.next(); 
                return new Entry<String, String>() {
                    @Override
                    public String getKey() {
                        return GitUtils.encode(entry.getKey());
                    }

                    @Override
                    public String getValue() {
                        return GitUtils.encode(entry.getValue());
                    }

                    @Override
                    public String setValue(String value) {
                        return null;
                    }
                };
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}