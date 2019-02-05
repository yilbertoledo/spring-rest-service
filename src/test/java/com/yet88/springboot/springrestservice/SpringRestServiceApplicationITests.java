package com.yet88.springboot.springrestservice;

import static org.hamcrest.CoreMatchers.is;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.yet88.springboot.springrestservice.model.Contact;

/**
 * Class to make integration tests for whole app
 * 
 * @author yilber.toledo
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
    public int port;

    @Value("${authentication.type}")
    private String authType;

    /**
     * Template used as rest client
     */
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    RestTemplate restTemplate;

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void getContact_BasicAuth() throws JSONException
    {
        Assume.assumeThat(getAuthType(), is(AuthType.BASIC));
        // Configure support for basic authentication
        testRestTemplate.getRestTemplate().getInterceptors().add(
                new BasicAuthenticationInterceptor(Constants.INMEMORY_ADMIN_NAME, Constants.INMEMORY_ADMIN_PASSWD));
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createUrl("/contacts/1"), HttpMethod.GET, entity,
                String.class);
        String expected = "{id:1,firstName:Juan,lastName:Villegas, phone:'+34867897788'}";
        // Assert for
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void postContact_BasicAuth() throws Exception
    {
        Assume.assumeThat(getAuthType(), is(AuthType.BASIC));
        testRestTemplate.getRestTemplate().getInterceptors().add(
                new BasicAuthenticationInterceptor(Constants.INMEMORY_ADMIN_NAME, Constants.INMEMORY_ADMIN_PASSWD));
        // Default mock Contact
        Contact contact = new Contact("Negro", "Primero", "+7890987654");
        HttpEntity<Contact> entity = new HttpEntity<Contact>(contact, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createUrl("/contacts"), HttpMethod.POST, entity,
                String.class);
        // Get location from response header
        String location = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
        Assert.assertTrue(location.contains("/contacts/"));
    }

    @Test
    public void getContact_DigestAuth() throws Exception
    {
        Assume.assumeThat(getAuthType(), is(AuthType.DIGEST));
        // Given
        // When
        ResponseEntity<String> response = restTemplate.exchange(createUrl("/contacts"), HttpMethod.GET, null,
                String.class);
        // Then
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void postContact_DigestAuth() throws Exception
    {
        Assume.assumeThat(getAuthType(), is(AuthType.DIGEST));
        // Default mock Contact
        Contact contact = new Contact("Negro", "Primero", "+7890987654");
        HttpEntity<Contact> entity = new HttpEntity<Contact>(contact, headers);
        ResponseEntity<String> response = restTemplate.exchange(createUrl("/contacts"), HttpMethod.POST, entity,
                String.class);
        // Get location from response header
        String location = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
        Assert.assertTrue(location.contains("/contacts/"));
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

    private enum AuthType
    {
        BASIC, DIGEST, JWT
    }

    private AuthType getAuthType()
    {
        AuthType authType = AuthType.BASIC;

        ResponseEntity<String> response = testRestTemplate.exchange(createUrl("/login"), HttpMethod.GET, null,
                String.class);
        if (response.getHeaders().containsKey(HttpHeaders.WWW_AUTHENTICATE))
        {
            if (response.getHeaders().get(HttpHeaders.WWW_AUTHENTICATE).toString().contains("Digest realm="))
                authType = AuthType.DIGEST;
            else if (response.getHeaders().get(HttpHeaders.WWW_AUTHENTICATE).toString().contains("Bearer "))
                authType = AuthType.JWT;
        }
        return authType;
    }
}
