package com.example.t1Backend.dal.repositories;

import com.example.t1Backend.dal.entities.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnologyRepo extends JpaRepository<Technology, Integer> {
    public boolean existsByName(String name);
    Technology findById(int id);

    Technology findByName(String name);
    List<Technology> findAllByRing(String ring);
    List<Technology> findAllBySection(String section);
    List<Technology> findAllByCategory(String category);

    void deleteByName(String name);
}
