package com.yet88.springboot.springrestservice.auth.digest;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * Subclass of HttpComponentsClientHttpRequestFactory to override the
 * createHttpContext method for Automatic Management of the Authorization HTTP
 * Header for Digest Authentication
 * 
 * @author yilbertoledo
 *
 */
public class HttpComponentsClientHttpRequestFactoryDigestAuth extends HttpComponentsClientHttpRequestFactory
{
    HttpHost host;

    public HttpComponentsClientHttpRequestFactoryDigestAuth(HttpHost host, HttpClient client)
    {
        super(client);
        this.host = host;
    }

    @Override
    protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri)
    {
        return createHttpContext();
    }

    private HttpContext createHttpContext()
    {
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate DIGEST scheme object, initialize it and add it to the local
        // auth cache
        DigestScheme digestAuth = new DigestScheme();
        // Override Realm if we already know the realm name
        digestAuth.overrideParamter("realm", com.yet88.springboot.springrestservice.Constants.DIGEST_REALM);
        authCache.put(host, digestAuth);

        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
        return localcontext;
    }
}