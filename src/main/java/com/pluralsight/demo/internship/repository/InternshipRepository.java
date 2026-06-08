package com.pluralsight.demo.internship.repository;

import com.pluralsight.demo.internship.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {
    // No custom queries yet
}
