package com.tperuch.assemblyservice.service;

import com.tperuch.assemblyservice.dto.SessionDto;
import com.tperuch.assemblyservice.dto.response.SessionResponseDto;
import com.tperuch.assemblyservice.entity.SessionEntity;
import com.tperuch.assemblyservice.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

import static com.tperuch.assemblyservice.stub.session.SessionDtoStub.getSessionDtoStub;
import static com.tperuch.assemblyservice.stub.session.SessionEntityStub.getSessionEntityStub;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private TopicService topicService;
    @Mock
    private SessionStatusService statusService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private SessionService sessionService;

    @Test
    void shouldFindAllSession() {
        List<SessionEntity> entites = List.of(getSessionEntityStub());
        when(sessionRepository.findAll()).thenReturn(entites);

        sessionService.findAllSessions();

        verify(sessionRepository,times(1)).findAll();
    }

    @Test
    void shouldOpenSession() {
        when(topicService.existsTopic(1L)).thenReturn(true);
        when(sessionRepository.existsByTopicId(1L)).thenReturn(false);
        when(sessionRepository.save(any())).thenReturn(getSessionEntityStub());
        doNothing().when(statusService).sendMessageToRabbitExchange(any());

        SessionResponseDto result = sessionService.openSession(getSessionDtoStub());

        verify(sessionRepository,times(1)).save(any());
        assertEquals(getSessionEntityStub().getId(), result.getId());
    }

    @Test
    void shouldOpenSessionWithDefaultTime() {
        SessionDto sessionDto = getSessionDtoStub();
        sessionDto.setSessionTimeInMinutes(null);

        when(topicService.existsTopic(1L)).thenReturn(true);
        when(sessionRepository.existsByTopicId(1L)).thenReturn(false);
        when(sessionRepository.save(any())).thenReturn(getSessionEntityStub());
        doNothing().when(statusService).sendMessageToRabbitExchange(any());

        SessionResponseDto result = sessionService.openSession(sessionDto);

        verify(sessionRepository,times(1)).save(any());
        assertEquals(getSessionEntityStub().getId(), result.getId());
    }

    @Test
    void shouldNotOpenSessionWhenTopicDontExists() {
        when(topicService.existsTopic(1L)).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> sessionService.openSession(getSessionDtoStub()));

        assertEquals("Nao existe pauta para o codigo informado - 1", exception.getMessage());
        assertEquals(exception.getClass().getName(), EntityNotFoundException.class.getName());
    }

    @Test
    void shouldNotOpenSessionWhenTopicIsAlreadyInVotingSession() {
        when(topicService.existsTopic(1L)).thenReturn(true);
        when(sessionRepository.existsByTopicId(1L)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> sessionService.openSession(getSessionDtoStub()));

        assertEquals("Pauta ja esta vinculada a uma sessao de votacao, favor escolher outra", exception.getMessage());
        assertEquals(exception.getClass().getName(), IllegalArgumentException.class.getName());
    }

    @Test
    void shouldFindExpiredSessions(){
        SessionEntity sessionEntity = getSessionEntityStub();
        sessionEntity.setVotingStart(LocalDateTime.now().minusMinutes(10L));
        sessionEntity.setVotingEnd(LocalDateTime.now().minusMinutes(5L));
        List<SessionEntity>entities = List.of(sessionEntity);

        when(sessionRepository.findByVotingEndLesserThanNowAndResultIsNull()).thenReturn(entities);

        sessionService.findExpiredSessions();

        verify(sessionRepository,times(1)).findByVotingEndLesserThanNowAndResultIsNull();
    }

}