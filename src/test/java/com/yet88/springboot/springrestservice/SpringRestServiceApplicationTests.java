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
 * Class to test methods in ContactRepository
 * 
 * @author yilbertoledo
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class SpringRestServiceApplicationTests
{
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContactRepository contactRepository;

    @Test
    public void whenFindByName_thenReturnEmployee()
    {
        // given
        Contact alex = new Contact("yet", "May", "89098");
        entityManager.persist(alex);
        entityManager.flush();

        // when
        Optional<Contact> found = contactRepository.findById(alex.getId());
        if (found.isPresent())
        {
            // then
            assertThat(found.get().getFirstName()).isEqualTo(alex.getFirstName());
        }
        else
        {
            assertTrue(false);
        }
    }
}
