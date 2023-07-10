package com.tperuch.assemblyservice.stub.topic;

import com.tperuch.assemblyservice.entity.TopicEntity;

public class TopicEntityStub {
    public static TopicEntity getTopicEntityStub(){
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setName("PAUTA 1");
        topicEntity.setId(1L);
        return topicEntity;
    }
}
