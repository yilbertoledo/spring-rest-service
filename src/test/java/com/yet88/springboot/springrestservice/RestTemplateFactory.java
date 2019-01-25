package com.yet88.springboot.springrestservice;

import org.apache.http.HttpHost;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Class for setting up the RestTemplate with Basic Authentication
 * 
 * @author yilbertoledo
 *
 */
@Configuration
public class RestTemplateFactory implements FactoryBean<RestTemplate>, InitializingBean
{
    /**
     * Autowire the port into the variable so that we can use it create the url.
     */
    @Value("${server.port}")
    public int port;

    private RestTemplate restTemplate;

    public RestTemplate getObject()
    {
        return restTemplate;
    }

    public Class<RestTemplate> getObjectType()
    {
        return RestTemplate.class;
    }

    public boolean isSingleton()
    {
        return true;
    }

    public void afterPropertiesSet()
    {
        HttpHost host = new HttpHost("localhost", port, "http");
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactoryBasicAuth(host));
    }
}
