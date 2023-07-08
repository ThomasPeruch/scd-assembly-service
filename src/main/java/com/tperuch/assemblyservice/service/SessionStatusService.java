package com.tperuch.assemblyservice.service;

import com.tperuch.assemblyservice.dto.SessionStatusDto;
import com.tperuch.assemblyservice.dto.response.SessionResponseDto;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SessionStatusService {
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.queue}")
    private String queue;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessageToRabbitExchange(SessionResponseDto sessionResponseDto){
        SessionStatusDto sessionStatusDto = buildStatusDto(sessionResponseDto);
        rabbitTemplate.convertAndSend(exchange, queue, convertObjToMessage(sessionStatusDto));
    }

    private SessionStatusDto buildStatusDto(SessionResponseDto sessionResponseDto) {
        SessionStatusDto sessionStatusDto = new SessionStatusDto();
        sessionStatusDto.setSessionId(sessionResponseDto.getId());
        sessionStatusDto.setStatus("OPEN");
        return sessionStatusDto;
    }

    private Message convertObjToMessage(SessionStatusDto dto) {
        String json = convertObjToJson(dto);
        return new Message(json.getBytes(), new MessageProperties());
    }

    private String convertObjToJson(SessionStatusDto dto) {
        return new JSONObject(dto).toString();
    }
}