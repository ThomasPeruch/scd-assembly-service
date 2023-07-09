package com.tperuch.assemblyservice.service;

import com.tperuch.assemblyservice.dto.SessionDto;
import com.tperuch.assemblyservice.dto.SessionStatusDto;
import com.tperuch.assemblyservice.dto.response.SessionResponseDto;
import com.tperuch.assemblyservice.entity.SessionEntity;
import com.tperuch.assemblyservice.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SessionService {
    private static final Long defaultSessionTime = 1L;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TopicService topicService;
    @Autowired
    private SessionStatusService statusService;

    public SessionResponseDto openSession(SessionDto sessionDto) {
        validateTopicForSessionOpening(sessionDto);
        SessionEntity sessionEntity = buildEntity(sessionDto);
        SessionResponseDto sessionResponseDto = buildResponseDto(saveSession(sessionEntity));
        statusService.sendMessageToRabbitExchange(buildStatusDto(sessionResponseDto));
        return sessionResponseDto;
    }

    public List<SessionEntity> findExpiredSessions(){
        return sessionRepository.findByVotingEndLesserThanNowAndResultIsNull();
    }

    public SessionEntity saveSession(SessionEntity sessionEntity){
        return sessionRepository.save(sessionEntity);
    }

    private void validateTopicForSessionOpening(SessionDto sessionDto) {
        verifyIfTopicExistsOrNot(sessionDto);
        isTopicAlreadyBindToASession(sessionDto.getTopicId());
    }

    private SessionStatusDto buildStatusDto(SessionResponseDto sessionResponseDto) {
        SessionStatusDto sessionStatusDto = new SessionStatusDto();
        sessionStatusDto.setSessionId(sessionResponseDto.getId());
        sessionStatusDto.setStatus("OPEN");
        return sessionStatusDto;
    }

    private void verifyIfTopicExistsOrNot(SessionDto sessionDto) {
        if (!topicService.existsTopic(sessionDto.getTopicId())) {
            throw new EntityNotFoundException("Não existe pauta para o codigo informado - " + sessionDto.getTopicId());
        }
    }

    private void isTopicAlreadyBindToASession(Long topicId) {
        if (sessionRepository.existsByTopicId(topicId)) {
            throw new IllegalArgumentException("Pauta ja está vinculada a uma sessão de votação, favor escolher outra");
        }
    }

    private SessionResponseDto buildResponseDto(SessionEntity savedEntity) {
        SessionResponseDto sessionResponseDto = new SessionResponseDto();
        sessionResponseDto.setId(savedEntity.getId());
        sessionResponseDto.setTopicId(savedEntity.getTopicId());
        sessionResponseDto.setVotingStart(savedEntity.getVotingStart().toString());
        sessionResponseDto.setVotingEnd(savedEntity.getVotingEnd().toString());
        return sessionResponseDto;
    }

    private SessionEntity buildEntity(SessionDto sessionDto) {
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setTopicId(sessionDto.getTopicId());
        LocalDateTime votingStartDate = LocalDateTime.now();
        sessionEntity.setVotingStart(votingStartDate);
        sessionEntity.setVotingEnd(getSessionTime(sessionDto.getSessionTimeInMinutes(), votingStartDate));
        return sessionEntity;
    }

    private LocalDateTime getSessionTime(Integer sessionTime, LocalDateTime votingStartDate) {
        if (Objects.isNull(sessionTime)) {
            return votingStartDate.plusMinutes(defaultSessionTime);
        }
        return votingStartDate.plusMinutes(sessionTime.longValue());
    }
}