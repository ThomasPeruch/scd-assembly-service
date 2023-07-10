package com.tperuch.assemblyservice.stub.topic;

import com.tperuch.assemblyservice.dto.response.TopicResponseDto;

public class TopicResponseDtoStub {
    public static TopicResponseDto getTopicResponseDtoStub(){
        TopicResponseDto responseDto = new TopicResponseDto();
        responseDto.setName("PAUTA 1");
        responseDto.setId(1L);
        return responseDto;
    }
}
