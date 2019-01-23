package com.yet88.springboot.springrestservice.model;

public class ContactModificable
{
    private String firstName;
    private String lastName;

    public ContactModificable()
    {
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

}
