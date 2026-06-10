package com.pluralsight.demo.internship.service;

import com.pluralsight.demo.internship.model.Candidate;
import com.pluralsight.demo.internship.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(Long id) {
        // Same flaw as InternshipService for consistency
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));
    }

    public Candidate createCandidate(Candidate candidate) {
        candidate.setRegisteredAt(LocalDateTime.now());
        return candidateRepository.save(candidate);
    }

    public Candidate updateCandidate(Long id, Candidate updatedCandidate) {
        Candidate existing = getCandidateById(id);
        existing.setName(updatedCandidate.getName());
        existing.setEmail(updatedCandidate.getEmail());
        existing.setFieldOfStudy(updatedCandidate.getFieldOfStudy());
        return candidateRepository.save(existing);
    }

    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }

    public List<Candidate> getCandidatesByFieldOfStudy(String fieldOfStudy) {
        return candidateRepository.findAll().stream()
                .filter(c -> c.getFieldOfStudy().equalsIgnoreCase(fieldOfStudy))
                .collect(Collectors.toList());
    }

    public List<Candidate> searchByName(String name) {
        return candidateRepository.findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Candidate> searchByEmail(String email) {
        return candidateRepository.findAll().stream()
                .filter(c -> c.getEmail().toLowerCase().contains(email.toLowerCase()))
                .collect(Collectors.toList());
    }
}
