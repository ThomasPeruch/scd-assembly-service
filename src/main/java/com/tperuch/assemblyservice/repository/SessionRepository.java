package com.tperuch.assemblyservice.repository;

import com.tperuch.assemblyservice.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    boolean existsByTopicId(Long topicId);
}
