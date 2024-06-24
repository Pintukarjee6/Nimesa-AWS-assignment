package com.nimesa_assignment.repository;

import com.nimesa_assignment.entity.Ec2Instance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Ec2InstanceRepository extends JpaRepository<Ec2Instance, Long> {
}
