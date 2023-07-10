package com.tperuch.assemblyservice.service;

import com.tperuch.assemblyservice.dto.SessionDto;
import com.tperuch.assemblyservice.dto.SessionStatusDto;
import com.tperuch.assemblyservice.dto.response.SessionResponseDto;
import com.tperuch.assemblyservice.entity.SessionEntity;
import com.tperuch.assemblyservice.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.tperuch.assemblyservice.util.DateUtil.formatDate;

@Service
public class SessionService {
    private static final Long defaultSessionTime = 1L;
    private static final String openSessionStatus = "OPEN";
    Logger logger = LoggerFactory.getLogger(SessionService.class);
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TopicService topicService;
    @Autowired
    private SessionStatusService statusService;
    @Autowired
    private ModelMapper modelMapper;

    public List<SessionResponseDto> findAllSessions() {
        List<SessionEntity> entities = sessionRepository.findAll();
        return entities.stream().map(this::buildResponseDto).toList();
    }

    public SessionResponseDto openSession(SessionDto sessionDto) {
        validateTopicForSessionOpening(sessionDto);
        SessionEntity sessionEntity = buildEntity(sessionDto);
        SessionResponseDto sessionResponseDto = buildResponseDto(saveSession(sessionEntity));
        statusService.sendMessageToRabbitExchange(buildStatusDto(sessionResponseDto));
        return sessionResponseDto;
    }

    public List<SessionEntity> findExpiredSessions() {
        return sessionRepository.findByVotingEndLesserThanNowAndResultIsNull();
    }

    public SessionEntity saveSession(SessionEntity sessionEntity) {
        logger.info("Salvando sessão de votação de pauta na base de dados. ID da sessão: {} - ID da pauta: {}",
                sessionEntity.getId(), sessionEntity.getTopicId());
        return sessionRepository.save(sessionEntity);
    }

    private void validateTopicForSessionOpening(SessionDto sessionDto) {
        logger.info("Validando se a pauta pode ser aberta para votação em uma sessão");
        verifyIfTopicExistsOrNot(sessionDto);
        isTopicAlreadyBindToASession(sessionDto.getTopicId());
    }

    private SessionStatusDto buildStatusDto(SessionResponseDto sessionResponseDto) {
        logger.info("Tratando dados para mensageria");
        SessionStatusDto sessionStatusDto = new SessionStatusDto();
        sessionStatusDto.setSessionId(sessionResponseDto.getId());
        sessionStatusDto.setStatus(openSessionStatus);
        return sessionStatusDto;
    }

    private void verifyIfTopicExistsOrNot(SessionDto sessionDto) {
        if (!topicService.existsTopic(sessionDto.getTopicId())) {
            logger.error("Nao existe pauta para o codigo informado - {}", sessionDto.getTopicId());
            throw new EntityNotFoundException("Nao existe pauta para o codigo informado - " + sessionDto.getTopicId());
        }
    }

    private void isTopicAlreadyBindToASession(Long topicId) {
        if (sessionRepository.existsByTopicId(topicId)) {
            logger.error("Pauta {} ja esta vinculada a uma sessão de votacao, favor escolher outra", topicId);
            throw new IllegalArgumentException("Pauta ja esta vinculada a uma sessao de votacao, favor escolher outra");
        }
    }

    private SessionResponseDto buildResponseDto(SessionEntity savedEntity) {
        SessionResponseDto sessionResponseDto = new SessionResponseDto();
        sessionResponseDto.setId(savedEntity.getId());
        sessionResponseDto.setTopicId(savedEntity.getTopicId());
        sessionResponseDto.setVotingStart(formatDate(savedEntity.getVotingStart()));
        sessionResponseDto.setVotingEnd(formatDate(savedEntity.getVotingEnd()));
        sessionResponseDto.setFinished(savedEntity.isFinished());
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