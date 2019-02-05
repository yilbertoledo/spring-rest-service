package com.yet88.springboot.springrestservice.auth.basic;

import org.apache.http.HttpHost;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Class for setting up the RestTemplate with Basic Authentication
 * 
 * @author yilber.toledo
 *
 */
@Configuration
@ConditionalOnProperty(value = "authentication.type", havingValue = "basic", matchIfMissing = true)
public class RestBasicAuthTemplateFactory implements FactoryBean<RestTemplate>, InitializingBean
{
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
