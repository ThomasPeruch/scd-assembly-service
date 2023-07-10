package com.tperuch.assemblyservice.service;

import com.google.gson.Gson;
import com.tperuch.assemblyservice.dto.SessionStatusDto;

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
    @Value("${spring.rabbitmq.session-status.queue}")
    private String queue;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Gson gson;

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
        return gson.toJson(dto);
    }
}