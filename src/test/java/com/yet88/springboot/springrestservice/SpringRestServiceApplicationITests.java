package com.yet88.springboot.springrestservice;

import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.yet88.springboot.springrestservice.model.Contact;

/**
 * Class to make integration tests for whole app
 * 
 * @author yilbertoledo
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringRestServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringRestServiceApplicationITests
{
    /**
     * Autowire the random port into the variable so that we can use it create
     * the url.
     */
    @LocalServerPort
    private int port;

    /**
     * Template used as rest client
     */
    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void getContact() throws JSONException
    {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createUrl("/contacts/1"), HttpMethod.GET, entity,
                String.class);
        String expected = "{id:1,firstName:Juan,lastName:Villegas, phone:'+34867897788'}";
        // Assert for
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void postContact() throws Exception
    {
        // Default mock Contact
        Contact contact = new Contact("Negro", "Primero", "+7890987654");
        HttpEntity<Contact> entity = new HttpEntity<Contact>(contact, headers);
        ResponseEntity<String> response = restTemplate.exchange(createUrl("/contacts"), HttpMethod.POST, entity,
                String.class);
        // Get location from response header
        String location = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
        assertTrue(location.contains("/contacts/"));
    }

    /**
     * Return complete URL. Protocol + host + port + given URI
     * 
     * @param uri: Resource locator part
     * @return
     */
    private String createUrl(String uri)
    {
        return "http://localhost:" + port + uri;
    }
}
