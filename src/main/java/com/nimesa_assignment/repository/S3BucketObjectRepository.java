package com.nimesa_assignment.repository;

import com.nimesa_assignment.entity.S3BucketObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface S3BucketObjectRepository extends JpaRepository<S3BucketObject, Long> {

    @Query("SELECT COUNT(s) FROM S3BucketObject s WHERE s.bucketName = :bucketName")
    long countByBucketName(@Param("bucketName") String bucketName);

    @Query("SELECT s.objectKey FROM S3BucketObject s WHERE s.bucketName = :bucketName AND s.objectKey LIKE %:pattern%")
    List<String> findByBucketNameAndObjectKeyLike(@Param("bucketName") String bucketName, @Param("pattern") String pattern);
}
