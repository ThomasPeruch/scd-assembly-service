package com.tperuch.assemblyservice.stub.session;

import com.tperuch.assemblyservice.entity.SessionEntity;

import java.time.LocalDateTime;

public class SessionEntityStub {
    public static SessionEntity getSessionEntityStub(){
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setTopicId(1L);
        sessionEntity.setVotingEnd(LocalDateTime.now());
        sessionEntity.setVotingStart(LocalDateTime.now().minusMinutes(1L));
        sessionEntity.setFinished(true);
        sessionEntity.setId(1L);
        return sessionEntity;
    }
}
