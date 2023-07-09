package com.tperuch.assemblyservice.schedule;

import com.tperuch.assemblyservice.dto.SessionStatusDto;
import com.tperuch.assemblyservice.entity.SessionEntity;
import com.tperuch.assemblyservice.service.SessionService;
import com.tperuch.assemblyservice.service.SessionStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(SessionExpireChecker.class);
    @Scheduled(cron = "15 * * * * *")
    public void checkSessionExpire(){
        logger.info("iniciando job para verificar se há sessões expiradas, ou seja, sessões que devem ser fechadas e envia-las como mensagem");
        List<SessionEntity> sessions = sessionService.findExpiredSessions();
        if(Objects.nonNull(sessions) && !sessions.isEmpty()){
            sessions.forEach(session -> {
                SessionStatusDto sessionStatus = buildSessionStatusDto(session);
                statusService.sendMessageToRabbitExchange(sessionStatus);
                updateSession(session);
            });
        }
        logger.info("encerrando job");
    }

    private void updateSession(SessionEntity session) {
        logger.info("Atualizando status da sessão de id {}",session.getId());
        SessionEntity entityToUpdate = session;
        entityToUpdate.setFinished(true);
        sessionService.saveSession(entityToUpdate);
    }

    private SessionStatusDto buildSessionStatusDto(SessionEntity session) {
        SessionStatusDto sessionStatusDto = new SessionStatusDto();
        sessionStatusDto.setSessionId(session.getId());
        sessionStatusDto.setStatus("CLOSED");
        logger.info("Dados mapeados envio de mensagem - {}",sessionStatusDto);
        return sessionStatusDto;
    }
}
