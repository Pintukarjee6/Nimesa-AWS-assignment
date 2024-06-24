package com.nimesa_assignment.controller;

import com.nimesa_assignment.entity.Ec2Instance;
import com.nimesa_assignment.entity.S3Bucket;
import com.nimesa_assignment.entity.Job;
import com.nimesa_assignment.repository.Ec2InstanceRepository;
import com.nimesa_assignment.repository.JobRepository;
import com.nimesa_assignment.repository.S3BucketObjectRepository;
import com.nimesa_assignment.repository.S3BucketRepository;
import com.nimesa_assignment.service.AwsDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AwsDiscoveryController {

    @Autowired
    private AwsDiscoveryService awsDiscoveryService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private Ec2InstanceRepository ec2InstanceRepository;

    @Autowired
    private S3BucketRepository s3BucketRepository;

    @Autowired
    private S3BucketObjectRepository s3BucketObjectRepository;

    @PostMapping("/discoverServices")
    public Long discoverServices(@RequestBody List<String> services) {

        Job job = new Job("In Progress", LocalDateTime.now());
        job = jobRepository.save(job);

        if (services.contains("EC2")) {
            awsDiscoveryService.discoverEc2Instances(job);
        }

        if (services.contains("S3")) {
            awsDiscoveryService.discoverS3Buckets(job);
        }
        return job.getId();
    }

    @GetMapping("/getJobResult/{jobId}")
    public String getJobResult(@PathVariable Long jobId) {
        return jobRepository.findById(jobId)
                .map(Job::getStatus)
                .orElse("Job Not Found");
    }

    @GetMapping("/getDiscoveryResult/{service}")
    public List<String> getDiscoveryResult(@PathVariable String service) {
        if (service.equalsIgnoreCase("EC2")) {
            return ec2InstanceRepository.findAll().stream()
                    .map(Ec2Instance::getInstanceId)
                    .collect(Collectors.toList());
        } else if (service.equalsIgnoreCase("S3")) {
            return s3BucketRepository.findAll().stream()
                    .map(S3Bucket::getBucketName)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Unsupported service: " + service);
        }
    }

    @PostMapping("/getS3BucketObjects")
    public Long getS3BucketObjects(@RequestParam String bucketName) {
        Job job = new Job("In Progress", LocalDateTime.now());
        job = jobRepository.save(job);

        awsDiscoveryService.discoverS3BucketObjects(bucketName, job);
        return job.getId();
    }

    @GetMapping("/getS3BucketObjectCount")
    public long getS3BucketObjectCount(@RequestParam String bucketName) {
        return s3BucketObjectRepository.countByBucketName(bucketName);
    }

    @GetMapping("/getS3BucketObjectlike")
    public List<String> getS3BucketObjectlike(@RequestParam String bucketName, @RequestParam String pattern) {
        return s3BucketObjectRepository.findByBucketNameAndObjectKeyLike(bucketName, pattern);
    }
}
