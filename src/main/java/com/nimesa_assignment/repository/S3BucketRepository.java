package com.nimesa_assignment.repository;

import com.nimesa_assignment.entity.S3Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3BucketRepository extends JpaRepository<S3Bucket, Long> {
}
