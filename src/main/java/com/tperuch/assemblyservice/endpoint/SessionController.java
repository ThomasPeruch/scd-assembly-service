package com.tperuch.assemblyservice.endpoint;

import com.tperuch.assemblyservice.dto.SessionDto;
import com.tperuch.assemblyservice.dto.response.SessionResponseDto;
import com.tperuch.assemblyservice.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponseDto> createVotingSession(@Valid @RequestBody SessionDto sessionDto) {
        return new ResponseEntity(sessionService.openSession(sessionDto), HttpStatus.CREATED);
    }
}
