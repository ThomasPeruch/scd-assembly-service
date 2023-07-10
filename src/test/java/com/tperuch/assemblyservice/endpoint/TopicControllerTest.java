package com.tperuch.assemblyservice.endpoint;

import com.google.gson.Gson;
import com.tperuch.assemblyservice.dto.TopicDto;
import com.tperuch.assemblyservice.dto.response.TopicResponseDto;
import com.tperuch.assemblyservice.service.TopicService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.tperuch.assemblyservice.stub.session.SessionResponseStubDto.getSessionResponseDtoStub;
import static com.tperuch.assemblyservice.stub.topic.TopicDtoStub.getTopicDtoStub;
import static com.tperuch.assemblyservice.stub.topic.TopicResponseDtoStub.getTopicResponseDtoStub;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TopicController.class)
@AutoConfigureMockMvc
class TopicControllerTest {
    @MockBean
    private TopicService service;
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldCreateTopic() throws Exception {
        TopicDto topicDto = getTopicDtoStub();
        Gson gson = new Gson();
        String json = gson.toJson(topicDto);
        TopicResponseDto responseDto = getTopicResponseDtoStub();

        when(service.createTopic(any())).thenReturn(responseDto);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/topic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("\"id\":"+responseDto.getId()));
    }

    @Test
    void shouldGetAllTopics() throws Exception {
        TopicResponseDto responseDto = getTopicResponseDtoStub();

        when(service.findAllTopics()).thenReturn(List.of(responseDto));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/topic")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("\"id\":"+responseDto.getId()));
    }

}