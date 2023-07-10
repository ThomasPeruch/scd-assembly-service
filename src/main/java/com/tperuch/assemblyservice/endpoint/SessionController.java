package com.tperuch.assemblyservice.endpoint;

import com.tperuch.assemblyservice.dto.SessionDto;
import com.tperuch.assemblyservice.dto.response.SessionResponseDto;
import com.tperuch.assemblyservice.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/session")
@Tag(name = "Sessões de votação", description = "Operações relacionadas as sessões de votação")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Operation(summary = "Cria uma sessão de votação")
    @PostMapping
    public ResponseEntity<SessionResponseDto> createVotingSession(@Valid @RequestBody SessionDto sessionDto) {
        return new ResponseEntity(sessionService.openSession(sessionDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Busca todas as sessões")
    @GetMapping
    public ResponseEntity<List<SessionResponseDto>> findAllSession() {
        return new ResponseEntity(sessionService.findAllSessions(), HttpStatus.OK);
    }
}
