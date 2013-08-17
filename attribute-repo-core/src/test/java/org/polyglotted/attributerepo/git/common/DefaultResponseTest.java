package org.polyglotted.attributerepo.git.common;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;

public class DefaultResponseTest {

    @Mocked
    HttpResponse mockResponse;

    @Test(expected = RuntimeException.class)
    public void testInternalError() {
        new DefaultResponse(mockResponse);
    }

    @Test
    public void testEmptyCodeGetMeta() {
        new Expectations() {
            {
                mockResponse.getStatusLine();
                result = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 204, null);

                mockResponse.getFirstHeader(anyString);
                result = new BasicHeader("header", "unit-test");
            }
        };
        DefaultResponse response = new DefaultResponse(mockResponse);
        assertEquals("Unknown error occurred (204)", response.getResponseStatus());
        assertEquals("unit-test", response.getMeta("header"));
    }

    @Test(expected = RuntimeException.class)
    public void testLesserError() {
        new Expectations() {
            {
                mockResponse.getStatusLine();
                result = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 199, null);
            }
        };
        new DefaultResponse(mockResponse);
    }
}
