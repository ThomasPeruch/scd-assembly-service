package com.tperuch.assemblyservice.service;

import com.tperuch.assemblyservice.dto.SessionDto;
import com.tperuch.assemblyservice.dto.response.SessionResponseDto;
import com.tperuch.assemblyservice.entity.SessionEntity;
import com.tperuch.assemblyservice.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class SessionService {
    private static final Long defaultSessionTime = 1L;
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.queue}")
    private String queue;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TopicService topicService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public SessionResponseDto openSession(SessionDto sessionDto) {
        validateTopicForSessionOpening(sessionDto);
        SessionEntity sessionEntity = buildEntity(sessionDto);
        SessionResponseDto sessionResponseDto = buildResponseDto(sessionRepository.save(sessionEntity));
        sendMessageToRabbitExchange(sessionResponseDto);
        return sessionResponseDto;
    }

    private void sendMessageToRabbitExchange(SessionResponseDto sessionResponseDto) {
        rabbitTemplate.convertAndSend(exchange, queue, convertObjToMessage(sessionResponseDto));
    }

    private Message convertObjToMessage(SessionResponseDto sessionResponseDto) {
        String json = convertObjToJson(sessionResponseDto);
        return new Message(json.getBytes(), new MessageProperties());
    }

    private String convertObjToJson(SessionResponseDto sessionResponseDto) {
        SessionResponseDto dtoToSend = new SessionResponseDto();
        dtoToSend.setTopicId(sessionResponseDto.getTopicId());
        return new JSONObject(dtoToSend).toString();
    }

    private void validateTopicForSessionOpening(SessionDto sessionDto) {
        verifyIfTopicExistsOrNot(sessionDto);
        isTopicAlreadyBindToASession(sessionDto.getTopicId());
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