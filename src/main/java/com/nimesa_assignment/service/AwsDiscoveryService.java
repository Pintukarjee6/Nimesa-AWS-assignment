package com.nimesa_assignment.service;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import com.nimesa_assignment.entity.Ec2Instance;
import com.nimesa_assignment.entity.Job;
import com.nimesa_assignment.entity.S3Bucket;
import com.nimesa_assignment.entity.S3BucketObject;
import com.nimesa_assignment.repository.Ec2InstanceRepository;
import com.nimesa_assignment.repository.JobRepository;
import com.nimesa_assignment.repository.S3BucketObjectRepository;
import com.nimesa_assignment.repository.S3BucketRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class AwsDiscoveryService {

    @Autowired
    private Ec2Client ec2Client;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private Ec2InstanceRepository ec2InstanceRepository;

    @Autowired
    private S3BucketRepository s3BucketRepository;

    @Autowired
    private S3BucketObjectRepository s3BucketObjectRepository;

    @Async
    @Transactional
    public CompletableFuture<Job> discoverEc2Instances(Job job) {
        try {
            DescribeInstancesRequest request = DescribeInstancesRequest.builder().build();
            DescribeInstancesResponse response = ec2Client.describeInstances(request);

            for (software.amazon.awssdk.services.ec2.model.Reservation reservation : response.reservations()) {
                for (software.amazon.awssdk.services.ec2.model.Instance instance : reservation.instances()) {
                    Ec2Instance ec2Instance = new Ec2Instance(instance.instanceId());
                    ec2InstanceRepository.save(ec2Instance);
                }
            }
            job.setStatus("Success");
            job.setEndTime(LocalDateTime.now());
        } catch (Exception e) {
            job.setStatus("Failed");
        }
        jobRepository.save(job);
        return CompletableFuture.completedFuture(job);
    }

    @Async
    @Transactional
    public CompletableFuture<Job> discoverS3Buckets(Job job) {
        try {
            ListBucketsResponse response = s3Client.listBuckets();
            for (software.amazon.awssdk.services.s3.model.Bucket bucket : response.buckets()) {
                S3Bucket s3Bucket = new S3Bucket(bucket.name());
                s3BucketRepository.save(s3Bucket);
            }

            job.setStatus("Success");
            job.setEndTime(LocalDateTime.now());
        } catch (Exception e) {
            job.setStatus("Failed");
        }

        jobRepository.save(job);
        return CompletableFuture.completedFuture(job);
    }

    @Async
    @Transactional
    public CompletableFuture<Job> discoverS3BucketObjects(String bucketName, Job job) {
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(bucketName).build();
            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            for (S3Object s3Object : response.contents()) {
                S3BucketObject s3BucketObject = new S3BucketObject(bucketName, s3Object.key());
                s3BucketObjectRepository.save(s3BucketObject);
            }

            job.setStatus("Success");
            job.setEndTime(LocalDateTime.now());
        } catch (Exception e) {
            job.setStatus("Failed");
        }
        jobRepository.save(job);
        return CompletableFuture.completedFuture(job);
    }
}