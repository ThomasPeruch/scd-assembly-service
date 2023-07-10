package com.tperuch.assemblyservice.stub.session;

import com.tperuch.assemblyservice.dto.SessionStatusDto;

public class SessionStatusDtoStub {
    public static SessionStatusDto getSessionStatusDtoStub(){
        SessionStatusDto sessionStatusDto = new SessionStatusDto();
        sessionStatusDto.setStatus("OPEN");
        sessionStatusDto.setSessionId(1L);
        return sessionStatusDto;
    }

}
