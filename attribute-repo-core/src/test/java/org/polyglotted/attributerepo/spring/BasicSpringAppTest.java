package org.polyglotted.attributerepo.spring;

import static com.google.common.base.Charsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.polyglotted.attributerepo.TestUtils.asStream;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.getIntProperty;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.notNullProperty;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.polyglotted.crypto.utils.IoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:/spring-test/basic-test-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class BasicSpringAppTest {

    @Autowired
    Person person;

    @Autowired
    ResourceValue resourceValue;

    @Autowired
    AttribRepoResourceFactory resourceFactory;

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
        assertEquals("test value", person.getKey());
        assertEquals(31, person.getAge());
    }

    @Test
    public void testLoadOtherProperties() {
        assertNotNull(resourceValue);

        Properties props = resourceValue.getProperties();
        assertNotNull(props);
        assertEquals("/tmp/zookeeper", notNullProperty(props, "dataDir"));
        assertEquals(2181, getIntProperty(props, "clientPort", 0));
    }

    @Test
    public void testLoadResources() throws Exception {
        String expected = new String(IoUtils.readBytes(asStream("files/cache-output.txt")), UTF_8);
        String actual = resourceFactory.loadResource("cache.xml");
        assertEquals(expected, actual);
    }
}
