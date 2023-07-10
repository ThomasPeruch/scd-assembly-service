package com.tperuch.assemblyservice.schedule;

import com.tperuch.assemblyservice.entity.SessionEntity;
import com.tperuch.assemblyservice.service.SessionService;
import com.tperuch.assemblyservice.service.SessionStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.tperuch.assemblyservice.stub.session.SessionEntityStub.getSessionEntityStub;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionExpireCheckerTest {
    @Mock
    private SessionService sessionService;
    @Mock
    private SessionStatusService statusService;
    @InjectMocks
    private SessionExpireChecker sessionExpireChecker;

    @Test
    void shouldFindExpiredSessionsAndSendItsStatusByMessage(){
        SessionEntity entity = getSessionEntityStub();
        entity.setFinished(false);
        List<SessionEntity> entityList = List.of(entity);
        SessionEntity entityToUpdate = entity;
        entityToUpdate.setFinished(true);

        when(sessionService.findExpiredSessions()).thenReturn(entityList);
        doNothing().when(statusService).sendMessageToRabbitExchange(any());
        when(sessionService.saveSession(entityToUpdate)).thenReturn(entityToUpdate);

        sessionExpireChecker.checkSessionExpire();

        verify(sessionService,times(1)).findExpiredSessions();
        verify(statusService,times(1)).sendMessageToRabbitExchange(any());
        verify(sessionService,times(1)).saveSession(any());

    }



}