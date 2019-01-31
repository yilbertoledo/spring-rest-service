package com.yet88.springboot.springrestservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.yet88.springboot.springrestservice.dao.ContactRepository;
import com.yet88.springboot.springrestservice.model.Contact;

/**
 * Class to make unit tests for methods in ContactRepository
 * 
 * @author yilbertoledo
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ContactServiceUTest
{
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContactRepository contactRepository;

    @Test
    public void findContactById()
    {
        // given
        Contact contTest = new Contact("yet", "May", "89098");
        entityManager.persist(contTest);
        entityManager.flush();

        // when
        Optional<Contact> found = contactRepository.findById(contTest.getId());
        if (found.isPresent())
            // then
            assertThat(found.get().getFirstName()).isEqualTo(contTest.getFirstName());
        else
            assertTrue(false);

    }
}
