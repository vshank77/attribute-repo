package org.polyglotted.attributerepo.spring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExceptionPathSpringTest {

    @Test
    public void testOnlySystemProperties() {
        ClassPathXmlApplicationContext context = null;
        try {
            System.setProperty("attributerepo.ignore.git.properties", "true");
            System.setProperty("person.name", "john doe");
            System.setProperty("person.age", "25");
            System.setProperty("global.key", "value");

            context = new ClassPathXmlApplicationContext("/spring-test/basic-test-context.xml");
            Person person = context.getBean(Person.class);
            assertEquals("john doe", person.getName());
            assertEquals(25, person.getAge());
            assertEquals("value", person.getKey());
        }
        finally {
            safeClose(context);
            System.clearProperty("person.name");
            System.clearProperty("person.age");
            System.clearProperty("global.key");
            System.clearProperty("attributerepo.ignore.git.properties");
        }
    }

    @Test
    public void testOverrideFileProperties() {
        ClassPathXmlApplicationContext context = null;
        try {
            System.setProperty("attributerepo.override.file.location", "classpath:/files/override.properties");

            context = new ClassPathXmlApplicationContext("/spring-test/basic-test-context.xml");
            Person person = context.getBean(Person.class);
            assertEquals("joe bloggs", person.getName());
            assertEquals(52, person.getAge());
            assertEquals("value", person.getKey());
        }
        finally {
            safeClose(context);
            System.clearProperty("attributerepo.override.file.location");
        }
    }

    @Test
    public void testSystemPropertyOverrideFileProperties() {
        ClassPathXmlApplicationContext context = null;
        try {
            System.setProperty("attributerepo.override.file.location", "classpath:/files/override.properties");
            System.setProperty("person.age", "25");

            context = new ClassPathXmlApplicationContext("/spring-test/basic-test-context.xml");
            Person person = context.getBean(Person.class);
            assertEquals("joe bloggs", person.getName());
            assertEquals(25, person.getAge());
            assertEquals("value", person.getKey());
        }
        finally {
            safeClose(context);
            System.clearProperty("attributerepo.override.file.location");
            System.clearProperty("person.age");
        }
    }

    private void safeClose(ClassPathXmlApplicationContext context) {
        if (context != null)
            context.close();
    }
}
