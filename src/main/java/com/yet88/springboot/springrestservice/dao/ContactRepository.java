package com.yet88.springboot.springrestservice.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.yet88.springboot.springrestservice.model.Contact;

@Repository
@RepositoryRestResource(path = "contacts", collectionResourceRel = "contacts", itemResourceRel = "contact")
public interface ContactRepository extends PagingAndSortingRepository<Contact, Long>
{
}
