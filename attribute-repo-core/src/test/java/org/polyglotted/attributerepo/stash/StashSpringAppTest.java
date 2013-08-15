package org.polyglotted.attributerepo.stash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.polyglotted.attributerepo.spring.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:/spring-test/basic-test-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class StashSpringAppTest {

    @Autowired
    Person person;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty("attributerepo.properties.file.location", "classpath:/files/stash-integration.properties");
    }

    @AfterClass
    public static void tearUpAfterClass() {
        System.clearProperty("attributerepo.properties.file.location");
    }

    @Test
    @Ignore
    public void testIntegration() {
        assertNotNull(person);
        assertEquals("adrian tester", person.getName());
        assertEquals(28, person.getAge());
    }
}
