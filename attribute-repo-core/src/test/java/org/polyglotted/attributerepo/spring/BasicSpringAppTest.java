package org.polyglotted.attributerepo.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:/spring-test/basic-test-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class BasicSpringAppTest {

	@Autowired
	Person person;
	
    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty("attributerepo.properties.file.location", "classpath:/files/github-integration.properties");
    }

    @AfterClass
    public static void tearUpAfterClass() {
        System.clearProperty("attributerepo.properties.file.location");
    }


    @Test
    public void testIntegration() {
        assertNotNull(person);
        assertEquals("david tester", person.getName());
        assertEquals(31, person.getAge());
    }
}
