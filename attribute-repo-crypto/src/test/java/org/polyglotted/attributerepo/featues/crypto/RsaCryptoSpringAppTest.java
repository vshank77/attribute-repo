package org.polyglotted.attributerepo.featues.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:/spring-test/rsa-crypto-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class RsaCryptoSpringAppTest {

    @Autowired
    Person person;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty("attributerepo.override.file.location", "classpath:/files/rsa-cipher.properties");
    }

    @AfterClass
    public static void tearUpAfterClass() {
        System.clearProperty("attributerepo.override.file.location");
    }

    @Test
    public void testIntegration() {
        assertNotNull(person);
        assertEquals("Joe bloggs", person.getName());
        assertEquals(42, person.getAge());
        assertEquals("testPwd", person.getPassword());
    }
}