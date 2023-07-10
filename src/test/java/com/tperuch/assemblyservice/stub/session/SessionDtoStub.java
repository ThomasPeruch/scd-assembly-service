package com.tperuch.assemblyservice.stub.session;

import com.tperuch.assemblyservice.dto.SessionDto;

public class SessionDtoStub {
    public static SessionDto getSessionDtoStub(){
        SessionDto sessionDto = new SessionDto();
        sessionDto.setTopicId(1L);
        sessionDto.setSessionTimeInMinutes(1);
        return sessionDto;
    }
}
