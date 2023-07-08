package com.tperuch.assemblyservice.service;

import com.tperuch.assemblyservice.entity.TopicEntity;
import com.tperuch.assemblyservice.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public TopicEntity createTopic(TopicEntity topicEntity){
        return topicRepository.save(topicEntity);
    }

    public boolean existsTopic(Long id){
        return topicRepository.existsById(id);
    }

}
