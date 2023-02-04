package com.skillup.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillup.entity.DistributorSequence;

public interface DistributedSequenceRepository extends JpaRepository<DistributorSequence, Long> {

}
