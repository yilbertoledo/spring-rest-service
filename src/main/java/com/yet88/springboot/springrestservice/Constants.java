package com.yet88.springboot.springrestservice;

public final class Constants
{
    public static final String DIGEST_REALM = "contacts.yet.com";
    public static final String DIGEST_KEY = "MyTop&SupeSecretKey";
    
    public static final String INMEMORY_USER_NAME = "test-user";
    public static final String INMEMORY_USER_PASSWD = "test-password";
    public static final String INMEMORY_ADMIN_NAME = "admin";
    public static final String INMEMORY_ADMIN_PASSWD = "adminPass";
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    // JWT Constants
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
    public static final String SIGNING_KEY = "yilber.toledo";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "scopes";
}
