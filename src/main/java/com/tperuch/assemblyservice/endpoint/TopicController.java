package com.tperuch.assemblyservice.endpoint;

import com.tperuch.assemblyservice.dto.TopicDto;
import com.tperuch.assemblyservice.dto.response.TopicResponseDto;
import com.tperuch.assemblyservice.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private TopicService service;

    @PostMapping
    public ResponseEntity<TopicResponseDto> createTopic(@RequestBody TopicDto topicDto) {
        return new ResponseEntity<>(service.createTopic(topicDto), HttpStatus.CREATED);
    }
}
