package com.phone_myat.ticketapp.services.impl;

import com.phone_myat.ticketapp.domain.CreateEventRequest;
import com.phone_myat.ticketapp.domain.entities.Event;
import com.phone_myat.ticketapp.domain.entities.TicketType;
import com.phone_myat.ticketapp.domain.entities.User;
import com.phone_myat.ticketapp.repositories.EventRepository;
import com.phone_myat.ticketapp.repositories.UserRepository;
import com.phone_myat.ticketapp.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest eventRequest) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with id %s not found", organizerId)
                ));

        List<TicketType> ticketTypesToCreate = eventRequest.getTicketTypes().stream().map(
                ticketType ->
                {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    return ticketTypeToCreate;
                }
        ).toList();

        Event eventToCrete = new Event();
        eventToCrete.setName(eventRequest.getName());
        eventToCrete.setStart(eventRequest.getStart());
        eventToCrete.setEnd(eventRequest.getEnd());
        eventToCrete.setVenue(eventRequest.getVenue());
        eventToCrete.setSalesStart(eventRequest.getSaleStart());
        eventToCrete.setSalesEnd(eventRequest.getSaleEnd());
        eventToCrete.setStatus(eventRequest.getStatus());
        eventToCrete.setOrganizer(organizer);

        // Set both side relationship
        eventToCrete.setTicketTypes(ticketTypesToCreate);

        return eventRepository.save(eventToCrete);
    }
}
