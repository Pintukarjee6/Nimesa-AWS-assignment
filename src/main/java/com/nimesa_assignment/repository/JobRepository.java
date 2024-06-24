package com.nimesa_assignment.repository;

import com.nimesa_assignment.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
