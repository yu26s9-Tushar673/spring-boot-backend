package com.pluralsight.demo.internship.repository;

import com.pluralsight.demo.internship.model.Internship;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InternshipRepositoryTest {

    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void save_shouldPersistInternship() {
        // ARRANGE
        Internship internship = new Internship("Java Developer", "Tech Corp",
                "Build apps", "Amsterdam");
        internship.setPublished(true);

        // ACT
        Internship saved = internshipRepository.save(internship);

        // ASSERT
        assertNotNull(saved.getId());  // ID was generated
        assertEquals("Java Developer", saved.getTitle());

        // Verify it's in database
        Internship found = entityManager.find(Internship.class, saved.getId());
        assertNotNull(found);
        assertEquals("Java Developer", found.getTitle());
    }

    @Test
    void findById_shouldReturnInternship() {
        // ARRANGE
        Internship internship = new Internship("Data Analyst", "Data Inc",
                "Analyze", "Rotterdam");
        Internship saved = entityManager.persistAndFlush(internship);

        // ACT
        Optional<Internship> found = internshipRepository.findById(saved.getId());

        // ASSERT
        assertTrue(found.isPresent());
        assertEquals("Data Analyst", found.get().getTitle());
    }

    @Test
    void findAll_shouldReturnAllInternships() {
        // ARRANGE
        entityManager.persist(new Internship("Internship 1", "Company 1", "Desc", "Loc"));
        entityManager.persist(new Internship("Internship 2", "Company 2", "Desc", "Loc"));
        entityManager.persist(new Internship("Internship 3", "Company 3", "Desc", "Loc"));
        entityManager.flush();

        // ACT
        List<Internship> all = internshipRepository.findAll();

        // ASSERT
        assertEquals(3, all.size());
    }

    @Test
    void delete_shouldRemoveInternship() {
        // ARRANGE
        Internship internship = new Internship("To Delete", "Company", "Desc", "Loc");
        Internship saved = entityManager.persistAndFlush(internship);
        Long id = saved.getId();

        // ACT
        internshipRepository.deleteById(id);
        entityManager.flush();

        // ASSERT
        Optional<Internship> found = internshipRepository.findById(id);
        assertFalse(found.isPresent());  // Deleted!
    }
}