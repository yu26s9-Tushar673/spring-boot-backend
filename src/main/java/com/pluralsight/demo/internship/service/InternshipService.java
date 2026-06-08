package com.pluralsight.demo.internship.service;

import com.pluralsight.demo.internship.model.Internship;
import com.pluralsight.demo.internship.repository.InternshipRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InternshipService {

    private final InternshipRepository internshipRepository;
    
    @Value("${internships.auto-publish}")
    private boolean autoPublish;

    public InternshipService(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    public List<Internship> getAllInternships() {
        // Intentional flaw: filters out unpublished, might not be what we want
        return internshipRepository.findAll().stream()
                .filter(i -> i.isPublished())
                .collect(Collectors.toList());
    }

    public Internship getInternshipById(Long id) {
        // Intentional flaw: throws RuntimeException instead of proper exception
        return internshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship not found with id: " + id));
    }

    public Internship createInternship(Internship internship) {
        // Apply auto-publish config
        if (autoPublish) {
            internship.setPublished(true);
        }
        return internshipRepository.save(internship);
    }

    public Internship updateInternship(Long id, Internship updatedInternship) {
        Internship existing = getInternshipById(id);
        existing.setTitle(updatedInternship.getTitle());
        existing.setCompany(updatedInternship.getCompany());
        existing.setDescription(updatedInternship.getDescription());
        existing.setLocation(updatedInternship.getLocation());
        existing.setPublished(updatedInternship.isPublished());
        return internshipRepository.save(existing);
    }

    public void deleteInternship(Long id) {
        internshipRepository.deleteById(id);
    }
}
