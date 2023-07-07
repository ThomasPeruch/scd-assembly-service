package com.tperuch.assemblyservice.facade;

import com.tperuch.assemblyservice.dto.TopicDto;
import com.tperuch.assemblyservice.dto.response.TopicResponseDto;
import com.tperuch.assemblyservice.entity.TopicEntity;
import com.tperuch.assemblyservice.service.TopicService;
import com.tperuch.assemblyservice.util.TopicUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicFacade {

    @Autowired
    private TopicService topicService;

    @Autowired
    private ModelMapper modelMapper;

    public TopicResponseDto createTopic(TopicDto topicDto){
        TopicEntity topicEntity = topicService.createTopic(TopicUtil.mapToEntity(topicDto));
        return modelMapper.map(topicEntity, TopicResponseDto.class);
    }
}
