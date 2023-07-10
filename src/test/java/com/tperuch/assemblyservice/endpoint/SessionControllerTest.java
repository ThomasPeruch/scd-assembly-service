package com.tperuch.assemblyservice.endpoint;

import com.google.gson.Gson;
import com.tperuch.assemblyservice.dto.SessionDto;
import com.tperuch.assemblyservice.service.SessionService;
import com.tperuch.assemblyservice.stub.session.SessionDtoStub;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.tperuch.assemblyservice.stub.session.SessionDtoStub.getSessionDtoStub;
import static com.tperuch.assemblyservice.stub.session.SessionResponseStubDto.getSessionResponseDtoStub;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc
class SessionControllerTest {

    @MockBean
    private SessionService sessionService;
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldCreateVotingSession() throws Exception {
        SessionDto sessionDto = getSessionDtoStub();
        Gson gson = new Gson();
        String json = gson.toJson(sessionDto);

        when(sessionService.openSession(sessionDto)).thenReturn(getSessionResponseDtoStub());

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("\"id\":"+getSessionResponseDtoStub().getId()));
    }

    @Test
    void shouldFindAllSessions() throws Exception {
        when(sessionService.findAllSessions()).thenReturn(List.of(getSessionResponseDtoStub()));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/session")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("\"id\":"+getSessionResponseDtoStub().getId()));
    }

    @Test
    void shouldNotCreateVotingSessionWhenTopicDoNotExists() throws Exception {
        SessionDto sessionDto = getSessionDtoStub();
        Gson gson = new Gson();
        String json = gson.toJson(sessionDto);
        when(sessionService.openSession(sessionDto)).thenThrow(new EntityNotFoundException("Nao existe pauta para o codigo informado - 1"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof EntityNotFoundException))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Nao existe pauta para o codigo informado - 1"));
    }

    @Test
    void shouldNotCreateVotingSessionWhenTopicIsAlreadyInVotingSession() throws Exception {
        SessionDto sessionDto = getSessionDtoStub();
        Gson gson = new Gson();
        String json = gson.toJson(sessionDto);
        when(sessionService.openSession(sessionDto)).thenThrow(new IllegalArgumentException("Pauta ja esta vinculada a uma sessao de votacao, favor escolher outra"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof IllegalArgumentException))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Pauta ja esta vinculada a uma sessao de votacao, favor escolher outra"));
    }

}