package com.tperuch.assemblyservice.util;

import com.tperuch.assemblyservice.dto.TopicDto;
import com.tperuch.assemblyservice.entity.TopicEntity;

public class TopicUtil {

    public static TopicEntity mapToEntity(TopicDto topicDto){
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setName(topicDto.getName());
        return topicEntity;
    }
}
