package com.tperuch.assemblyservice.endpoint;

import com.tperuch.assemblyservice.dto.TopicDto;
import com.tperuch.assemblyservice.dto.response.TopicResponseDto;
import com.tperuch.assemblyservice.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic")
@Tag(name = "Pautas", description = "Operações relacionadas somente a pautas")
public class TopicController {
    @Autowired
    private TopicService service;

    @Operation(summary = "Cria uma pauta")
    @PostMapping
    public ResponseEntity<TopicResponseDto> createTopic(@RequestBody TopicDto topicDto) {
        return new ResponseEntity<>(service.createTopic(topicDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Busca todos os pautas")
    @GetMapping
    public ResponseEntity<List<TopicResponseDto>> findAllTopics() {
        return new ResponseEntity<>(service.findAllTopics(), HttpStatus.OK);
    }
}
