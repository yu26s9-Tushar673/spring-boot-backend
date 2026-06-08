package com.pluralsight.demo.internship.service;

import com.pluralsight.demo.internship.model.Internship;
import com.pluralsight.demo.internship.repository.InternshipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InternshipServiceTest {

    @Mock
    private InternshipRepository internshipRepository;

    @InjectMocks
    private InternshipService internshipService;

    @Test
    void getAllInternships_shouldReturnOnlyPublished() {
        // ARRANGE
        Internship published1 = new Internship("Title 1", "Company 1", "Desc", "Location");
        published1.setPublished(true);

        Internship published2 = new Internship("Title 2", "Company 2", "Desc", "Location");
        published2.setPublished(true);

        Internship unpublished = new Internship("Title 3", "Company 3", "Desc", "Location");
        unpublished.setPublished(false);

        List<Internship> allInternships = Arrays.asList(published1, published2, unpublished);

        when(internshipRepository.findAll()).thenReturn(allInternships);

        // ACT
        List<Internship> result = internshipService.getAllInternships();

        // ASSERT
        assertEquals(2, result.size());  // Only 2 published
        assertTrue(result.contains(published1));
        assertTrue(result.contains(published2));
        assertFalse(result.contains(unpublished));  // Filtered out!
    }

    @Test
    void createInternship_whenAutoPublishTrue_shouldPublish() {
        // ARRANGE
        // Set the auto-publish field to true (use reflection or setter)
        ReflectionTestUtils.setField(internshipService, "autoPublish", true);

        Internship inputInternship = new Internship("Title", "Company", "Desc", "Location");
        inputInternship.setPublished(false);  // Not published initially

        Internship savedInternship = new Internship("Title", "Company", "Desc", "Location");
        savedInternship.setId(1L);
        savedInternship.setPublished(true);  // Should be published

        when(internshipRepository.save(any(Internship.class))).thenReturn(savedInternship);

        // ACT
        Internship result = internshipService.createInternship(inputInternship);

        // ASSERT
        assertTrue(result.isPublished());  // Auto-published!
        verify(internshipRepository, times(1)).save(any(Internship.class));
    }

    @Test
    void createInternship_whenAutoPublishFalse_shouldNotPublish() {
        // ARRANGE
        ReflectionTestUtils.setField(internshipService, "autoPublish", false);

        Internship inputInternship = new Internship("Title", "Company", "Desc", "Location");
        inputInternship.setPublished(false);

        when(internshipRepository.save(any(Internship.class))).thenReturn(inputInternship);

        // ACT
        Internship result = internshipService.createInternship(inputInternship);

        // ASSERT
        assertFalse(result.isPublished());  // Not auto-published
    }
}