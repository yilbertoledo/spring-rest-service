package com.yet88.springboot.springrestservice.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.yet88.springboot.springrestservice.dao.ContactRepository;
import com.yet88.springboot.springrestservice.exception.ContactNotFoundException;
import com.yet88.springboot.springrestservice.model.Contact;
import com.yet88.springboot.springrestservice.model.ContactModificable;

@RestController
public class ContactController
{

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/contacts")
    public List<Contact> getAllContacts()
    {
        return contactRepository.findAll();
    }

    @GetMapping("/contacts/{id}")
    public Contact getContacts(@PathVariable(name = "id") long id)
    {
        Optional<Contact> contact = contactRepository.findById(id);
        if (!contact.isPresent())
            throw new ContactNotFoundException("id-" + id);
        return contact.get();
    }

    @DeleteMapping("/contacts/{id}")
    public void deleteContact(@PathVariable(name = "id") long id)
    {
        contactRepository.deleteById(id);
    }

    @PostMapping("/contacts")
    public ResponseEntity<Object> createContact(@RequestBody Contact contact)
    {
        Contact savedContact = contactRepository.save(contact);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedContact.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/contacts/{id}")
    public ResponseEntity<Object> replaceContact(@RequestBody Contact contact, @PathVariable long id)
    {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (!contactOptional.isPresent())
            return ResponseEntity.notFound().build();
        contact.setId(id);
        contactRepository.save(contact);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/contacts/{id}")
    public ResponseEntity<Object> partialUpdateContact(@RequestBody ContactModificable partialUpdate,
            @PathVariable("id") String id)
    {
        // Partial update of resources requires implementation
        // contactRepository.save(partialUpdate, id);
        return ResponseEntity.ok("resource address updated");
    }

}
