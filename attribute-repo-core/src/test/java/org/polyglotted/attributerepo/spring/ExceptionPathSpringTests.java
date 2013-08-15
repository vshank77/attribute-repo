package org.polyglotted.attributerepo.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExceptionPathSpringTests {

    @Test(expected = BeanCreationException.class)
    public void testNoLocationSupport() {
        ClassPathXmlApplicationContext context = null;
        try {
            context = new ClassPathXmlApplicationContext("/spring-test/noloc-test-context.xml");
            assertNotNull(context.getBean(PropertyPlaceholderConfigurer.class));

        } finally {
            safeClose(context);
        }
    }

    @Test
    public void testOnlySystemProperties() {
        ClassPathXmlApplicationContext context = null;
        try {
            System.setProperty("attributerepo.ignore.git.properties", "true");
            System.setProperty("person.name", "john doe");
            System.setProperty("person.age", "25");

            context = new ClassPathXmlApplicationContext("/spring-test/basic-test-context.xml");
            Person person = context.getBean(Person.class);
            assertEquals("john doe", person.getName());
            assertEquals(25, person.getAge());

        } finally {
            safeClose(context);
            System.clearProperty("person.name");
            System.clearProperty("person.age");
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

        } finally {
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

        } finally {
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
