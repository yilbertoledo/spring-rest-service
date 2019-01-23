package com.yet88.springboot.springrestservice;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.yet88.springboot.springrestservice.dao.ContactRepository;
import com.yet88.springboot.springrestservice.model.Contact;
import com.yet88.springboot.springrestservice.resource.ContactController;

/**
 * Class to Test REST Methods (endpoints) in ContactController
 * 
 * @author yilbertoledo
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ContactController.class, secure = false)
public class ContactControllerTest
{

    /**
     * Main entry point for server-side Spring MVC test support. It allows us to
     * execute requests against the test context.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Annotation used to add mocks to a Spring ApplicationContext.
     */
    @MockBean
    private ContactRepository contactRepository;

    @Test
    public void getContact() throws Exception
    {
        // Default mock Contact
        Contact mockContact = new Contact("Negro", "Primero", "+7890987654");
        mockContact.setId(1L);
        // Mock all invocations to contactRepository.findById
        Mockito.when(contactRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockContact));
        // Create request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/contacts/1").accept(MediaType.APPLICATION_JSON);
        // Perform request
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{id:1,firstName:'Negro',lastName:'Primero',phone:'+7890987654'}";
        // System.out.println(result.getResponse().getContentAsString());
        // Assert for response body
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void createContact() throws Exception
    {
        String exampleContactJson = "{\"id\":1,\"firstName\":\"Juan Pablo\",\"lastName\":\"Segundo\",\"phone\":\"+678987654\"}";
        // Default mock Contact
        Contact mockContact = new Contact("Negro", "Primero", "+7890987654");
        mockContact.setId(1L);
        // Mock all invocations to contactRepository.findById
        Mockito.when(contactRepository.save(Mockito.any(Contact.class))).thenReturn(mockContact);
        // Create request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/contacts").accept(MediaType.APPLICATION_JSON)
                .content(exampleContactJson).contentType(MediaType.APPLICATION_JSON);
        // Send request
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        // Get response
        MockHttpServletResponse response = result.getResponse();
        // Assert for status code
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        // Assert for resource location
        assertEquals("http://localhost/contacts/1", response.getHeader(HttpHeaders.LOCATION));
    }

}
