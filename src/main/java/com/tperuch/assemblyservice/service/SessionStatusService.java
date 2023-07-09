package com.tperuch.assemblyservice.service;

import com.tperuch.assemblyservice.dto.SessionStatusDto;
import com.tperuch.assemblyservice.dto.response.SessionResponseDto;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SessionStatusService {
    Logger logger = LoggerFactory.getLogger(SessionStatusService.class);
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.queue}")
    private String queue;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessageToRabbitExchange(SessionStatusDto sessionStatusDto){
        rabbitTemplate.convertAndSend(exchange, queue, convertObjToMessage(sessionStatusDto));
        logger.info("Mesagem enviada! EXCHANGE: {} - QUEUE/FILA: {}", exchange, queue);
    }

    private Message convertObjToMessage(SessionStatusDto dto) {
        logger.info("Transformando dados em Json para envio de mensagem");
        String json = convertObjToJson(dto);
        return new Message(json.getBytes(), new MessageProperties());
    }

    private String convertObjToJson(SessionStatusDto dto) {
        return new JSONObject(dto).toString();
    }
}