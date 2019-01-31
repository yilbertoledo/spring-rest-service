package com.yet88.springboot.springrestservice.auth.digest;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.yet88.springboot.springrestservice.Constants;

@Configuration
@ConditionalOnProperty(value = "authentication.type", havingValue = "digest", matchIfMissing = false)
public class RestDigestAuthTemplateFactory implements FactoryBean<RestTemplate>, InitializingBean
{
    @Value("${server.port}")
    public int port;

    private RestTemplate restTemplate;

    public RestDigestAuthTemplateFactory()
    {
        super();
    }

    public RestTemplate getObject() throws Exception
    {
        return restTemplate;
    }

    public Class<RestTemplate> getObjectType()
    {
        return RestTemplate.class;
    }

    public void afterPropertiesSet() throws Exception
    {
        HttpHost host = new HttpHost("localhost", port, "http");
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider())
                .useSystemProperties().build();
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactoryDigestAuth(host, client));
    }

    private CredentialsProvider provider()
    {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(Constants.INMEMORY_USER_NAME,
                Constants.INMEMORY_USER_PASSWD);
        provider.setCredentials(AuthScope.ANY, credentials);
        return provider;
    }

}
