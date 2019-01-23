package com.yet88.springboot.springrestservice.exception;

public class ContactNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = -6501015915507559289L;

    public ContactNotFoundException(String exception)
    {
        super(exception);
    }
}
