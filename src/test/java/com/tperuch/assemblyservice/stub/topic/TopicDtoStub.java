package com.tperuch.assemblyservice.stub.topic;

import com.tperuch.assemblyservice.dto.TopicDto;

public class TopicDtoStub {
    public static TopicDto getTopicDtoStub(){
        TopicDto topicDto = new TopicDto();
        topicDto.setName("PAUTA 1");
        return topicDto;
    }
}
