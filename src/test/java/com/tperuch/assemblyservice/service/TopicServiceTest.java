package com.tperuch.assemblyservice.service;

import com.tperuch.assemblyservice.dto.response.TopicResponseDto;
import com.tperuch.assemblyservice.entity.TopicEntity;
import com.tperuch.assemblyservice.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static com.tperuch.assemblyservice.stub.topic.TopicDtoStub.getTopicDtoStub;
import static com.tperuch.assemblyservice.stub.topic.TopicEntityStub.getTopicEntityStub;
import static com.tperuch.assemblyservice.stub.topic.TopicResponseDtoStub.getTopicResponseDtoStub;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private TopicService topicService;

    @Test
    void shouldCreateTopic() {
        TopicEntity topicEntity = getTopicEntityStub();
        when(topicRepository.save(any())).thenReturn(topicEntity);
        when(modelMapper.map(topicEntity, TopicResponseDto.class)).thenReturn(getTopicResponseDtoStub());

        TopicResponseDto responseDto = topicService.createTopic(getTopicDtoStub());

        assertEquals(getTopicEntityStub().getId(), responseDto.getId());
    }

    @Test
    void shouldFindAllTopics() {
        TopicEntity topicEntity = getTopicEntityStub();
        when(topicRepository.findAll()).thenReturn(List.of(topicEntity));
        when(modelMapper.map(topicEntity, TopicResponseDto.class)).thenReturn(getTopicResponseDtoStub());

        topicService.findAllTopics();

        verify(topicRepository, times(1)).findAll();
    }

    @Test
    void existsTopic() {
        when(topicRepository.existsById(1L)).thenReturn(true);

        topicService.existsTopic(1L);

        verify(topicRepository, times(1)).existsById(1L);
    }


}