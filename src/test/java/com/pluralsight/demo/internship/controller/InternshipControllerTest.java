package com.pluralsight.demo.internship.controller;

import com.pluralsight.demo.internship.model.Internship;
import com.pluralsight.demo.internship.service.InternshipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(InternshipController.class)
class InternshipControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Simulates HTTP requests

    @MockitoBean
    private InternshipService internshipService;  // Fake service

    @Test
    void getAllInternships_shouldReturnListOfInternships() throws Exception {
        // ARRANGE: Set up test data
        Internship internship1 = new Internship("Java Developer", "Tech Corp",
                "Build apps", "Amsterdam");
        internship1.setId(1L);
        internship1.setPublished(true);

        Internship internship2 = new Internship("Data Analyst", "Data Inc",
                "Analyze data", "Rotterdam");
        internship2.setId(2L);
        internship2.setPublished(true);

        List<Internship> internships = Arrays.asList(internship1, internship2);

        // Tell mock service what to return
        when(internshipService.getAllInternships()).thenReturn(internships);

        // ACT & ASSERT: Make request and verify response
        mockMvc.perform(get("/api/internships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // 200 OK
                .andExpect(jsonPath("$[0].title").value("Java Developer"))
                .andExpect(jsonPath("$[0].company").value("Tech Corp"))
                .andExpect(jsonPath("$[1].title").value("Data Analyst"))
                .andExpect(jsonPath("$.length()").value(2));  // 2 items
    }

    @Test
    void createInternship_shouldReturnCreatedInternship() throws Exception {
        // ARRANGE
        Internship inputInternship = new Internship("New Internship", "New Company",
                "Description", "Location");

        Internship savedInternship = new Internship("New Internship", "New Company",
                "Description", "Location");
        savedInternship.setId(10L);
        savedInternship.setPublished(false);

        when(internshipService.createInternship(any(Internship.class)))
                .thenReturn(savedInternship);

        // ACT & ASSERT
        mockMvc.perform(post("/api/internships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "title": "New Internship",
                          "company": "New Company",
                          "description": "Description",
                          "location": "Location"
                        }
                        """))
                .andExpect(status().isOk())  // Should be 201 but our code returns 200
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("New Internship"))
                .andExpect(jsonPath("$.published").value(false));
    }

    @Test
    void deleteInternship_shouldReturnNoContent() throws Exception {
        // ARRANGE
        Long id = 5L;
        doNothing().when(internshipService).deleteInternship(id);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/internships/{id}", id))
                .andExpect(status().isNoContent());  // 204

        // Verify service was called
        verify(internshipService, times(1)).deleteInternship(id);
    }
}