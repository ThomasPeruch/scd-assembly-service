package com.tperuch.assemblyservice.repository;

import com.tperuch.assemblyservice.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    boolean existsByTopicId(Long topicId);

    @Query(value = "select * from session_voting where voting_end < now() and is_finished is false", nativeQuery = true)
    List<SessionEntity> findByVotingEndLesserThanNowAndResultIsNull();
}
