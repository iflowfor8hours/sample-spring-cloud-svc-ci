package org.bk.producer.test;

import java.net.URI;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Integration test for webapp.
 */
public class AppTests extends Assert {


    @Test
    @Category(SmokeTest.class)
    public void smoke() throws Exception {
        testApp(20*1000);
    }

    @Test
    @Category(AcceptanceTest.class)
    public void acceptance() throws Exception {
        testApp(60*1000);
    }

    public void testApp(int duration) throws Exception {
        URI app = getSUT();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>("{\"message\": {\"payload\": \"test\"}}",headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> contents = restTemplate.postForEntity(app, entity, String.class);
        
        assertTrue(contents.getBody().contains("Hello!"));

        // this is supposed to be an integration test,
        // let's take some time. We want this to be longer than the build for sure.
        Thread.sleep(duration);
    }

    private URI getSUT() throws Exception {
        String url = System.getProperty("url");
        assertTrue("Subject under test should be passed in via -Durl=...", url!=null);
        return new URI(url);
    }
}
