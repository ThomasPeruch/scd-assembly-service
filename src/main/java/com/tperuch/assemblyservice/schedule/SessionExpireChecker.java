package com.tperuch.assemblyservice.schedule;

import com.tperuch.assemblyservice.dto.SessionStatusDto;
import com.tperuch.assemblyservice.entity.SessionEntity;
import com.tperuch.assemblyservice.service.SessionService;
import com.tperuch.assemblyservice.service.SessionStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SessionExpireChecker {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionStatusService statusService;

    @Scheduled(cron = "15 * * * * *")
    public void checkSessionExpire(){
        List<SessionEntity> sessions = sessionService.findExpiredSessions();
        if(Objects.nonNull(sessions) && !sessions.isEmpty()){
            sessions.forEach(session -> {
                SessionStatusDto sessionStatus = buildSessionStatusDto(session);
                statusService.sendMessageToRabbitExchange(sessionStatus);
                updateSession(session);
            });
        }
    }

    private void updateSession(SessionEntity session) {
        SessionEntity entityToUpdate = session;
        entityToUpdate.setFinished(true);
        sessionService.saveSession(entityToUpdate);
    }

    private SessionStatusDto buildSessionStatusDto(SessionEntity session) {
        SessionStatusDto sessionStatusDto = new SessionStatusDto();
        sessionStatusDto.setSessionId(session.getId());
        sessionStatusDto.setStatus("CLOSED");
        return sessionStatusDto;
    }
}
