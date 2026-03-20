package com.phone_myat.ticketapp.controller;

import com.phone_myat.ticketapp.domain.CreateEventRequest;
import com.phone_myat.ticketapp.domain.dtos.CreateEventRequestDto;
import com.phone_myat.ticketapp.domain.dtos.CreateEventResponseDto;
import com.phone_myat.ticketapp.domain.entities.Event;
import com.phone_myat.ticketapp.mappers.EventMapper;
import com.phone_myat.ticketapp.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private EventMapper eventMapper;
    private EventService eventService;

    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(@AuthenticationPrincipal Jwt jwt,
                                                              @Valid @RequestBody CreateEventRequestDto dto) {

        // Convert DTO to domain object
        CreateEventRequest createEventRequest = eventMapper.fromDto(dto);

        // Extract user id from jwt
        UUID userId = UUID.fromString(jwt.getSubject());

        // Create the event
        Event createdEvent = eventService.createEvent(userId, createEventRequest);

        // Convert response to DTO
        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createdEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(createEventResponseDto);

    }

}
