package com.pluralsight.demo.internship.controller;

import com.pluralsight.demo.internship.model.Candidate;
import com.pluralsight.demo.internship.service.CandidateService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CandidateController.class)
class CandidateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CandidateService candidateService;

    @Test
    void getAllCandidates_shouldReturnListOfCandidates() throws Exception {
        Candidate candidate1 = new Candidate(
                "John Doe",
                "johnDoe@email.com",
                "Computer Science");

        Candidate candidate2 = new Candidate(
                "Jane Doe",
                "janeDoe@email.com",
                "Software Engineering");

        List<Candidate> candidates = Arrays.asList(candidate1, candidate2);

        when(candidateService.getAllCandidates()).thenReturn(candidates);

        mockMvc.perform(get("/api/candidates")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("johnDoe@email.com"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getCandidateByID_shouldReturnCandidate() throws Exception {
        Candidate candidate = new Candidate(
                "John Doe",
                "johnDoe@email.com",
                "Computer Science");

        candidate.setId(1L);

        when(candidateService.getCandidateById(1L)).thenReturn(candidate);

        mockMvc.perform(get("/api/candidates/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johnDoe@email.com"))
                .andExpect(jsonPath("$.fieldOfStudy").value("Computer Science"));
    }

    @Test
    void createCandidate_shouldReturnCreatedCandidate() throws Exception {
        Candidate savedCandidate = new Candidate(
                "John Doe",
                "johnDoe@email.com",
                "Computer Science");
        savedCandidate.setId(1L);

        when(candidateService.createCandidate(any(Candidate.class))).thenReturn(savedCandidate);

        mockMvc.perform(post("/api/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name" : "John Doe",
                            "email" : "johnDoe@email.com",
                            "fieldOfStudy" : "Computer Science"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johnDoe@email.com"))
                .andExpect(jsonPath("$.fieldOfStudy").value("Computer Science"));
    }

    @Test
    void deleteCandidate_shouldReturnNoContent() throws Exception {
        Long id = 1L;

        doNothing().when(candidateService).deleteCandidate(id);

        mockMvc.perform(delete("/api/candidates/{id}", id)).andExpect(status().isNoContent());
        verify(candidateService, times(1)).deleteCandidate(id);
    }
}

