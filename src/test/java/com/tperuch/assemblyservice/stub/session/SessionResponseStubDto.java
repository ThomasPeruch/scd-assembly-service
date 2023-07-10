package com.tperuch.assemblyservice.stub.session;

import com.tperuch.assemblyservice.dto.response.SessionResponseDto;

import java.time.LocalDateTime;

public class SessionResponseStubDto {
    public static SessionResponseDto getSessionResponseDtoStub(){
        SessionResponseDto sessionResponseDto = new SessionResponseDto();
        sessionResponseDto.setTopicId(1L);
        sessionResponseDto.setId(1L);
        sessionResponseDto.setVotingStart(LocalDateTime.now().toString());
        sessionResponseDto.setVotingEnd(LocalDateTime.now().plusMinutes(1).toString());
        return sessionResponseDto;
    }
}
