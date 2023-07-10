package com.tperuch.assemblyservice.service;

import com.google.gson.Gson;
import com.tperuch.assemblyservice.dto.SessionStatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static com.tperuch.assemblyservice.stub.session.SessionStatusDtoStub.getSessionStatusDtoStub;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionStatusServiceTest {
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private Gson gson;
    @InjectMocks
    private SessionStatusService statusService;

    @BeforeEach
    void setUp(){
        ReflectionTestUtils.setField(statusService, "queue", "queue");
        ReflectionTestUtils.setField(statusService, "exchange", "exchange");
    }

    @Test
    void shouldSendMessageToRabbitExchange(){
        SessionStatusDto dto = getSessionStatusDtoStub();
        Gson _gson = new Gson();
        String json = _gson.toJson(dto);
        Message message = new Message(json.getBytes(),new MessageProperties());

        doNothing().when(rabbitTemplate).convertAndSend("exchange","queue",message);
        when(gson.toJson(dto)).thenReturn(json);

        statusService.sendMessageToRabbitExchange(dto);

        verify(rabbitTemplate,times(1)).convertAndSend("exchange","queue",message);
    }
}