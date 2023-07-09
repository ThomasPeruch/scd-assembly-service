package com.tperuch.assemblyservice.service;

import com.tperuch.assemblyservice.dto.TopicDto;
import com.tperuch.assemblyservice.dto.response.TopicResponseDto;
import com.tperuch.assemblyservice.entity.TopicEntity;
import com.tperuch.assemblyservice.repository.TopicRepository;
import com.tperuch.assemblyservice.util.TopicUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ModelMapper modelMapper;

    public TopicResponseDto createTopic(TopicDto topicDto){
        TopicEntity topicEntity = mapToEntity(topicDto);
        TopicEntity savedEntity = topicRepository.save(topicEntity);
        return modelMapper.map(savedEntity, TopicResponseDto.class);
    }

    public boolean existsTopic(Long id){
        return topicRepository.existsById(id);
    }

    private TopicEntity mapToEntity(TopicDto topicDto){
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setName(topicDto.getName());
        return topicEntity;
    }
}
