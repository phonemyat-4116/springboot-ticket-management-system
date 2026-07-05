package com.phone_myat.ticketapp.services.impl;

import com.phone_myat.ticketapp.domain.requests.CreateEventRequest;
import com.phone_myat.ticketapp.domain.entities.Event;
import com.phone_myat.ticketapp.domain.entities.User;
import com.phone_myat.ticketapp.exceptions.UserNotFoundException;
import com.phone_myat.ticketapp.mappers.EventMapper;
import com.phone_myat.ticketapp.mappers.TicketMapper;
import com.phone_myat.ticketapp.repositories.EventRepository;
import com.phone_myat.ticketapp.repositories.UserRepository;
import com.phone_myat.ticketapp.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final TicketMapper ticketMapper;

    @Transactional
    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest eventRequest) {

        User organizer = findOrganizerId(organizerId);

        // Instead of manual, use ticketMapper.toEntities()
//        List<TicketType> ticketTypesToCreate = eventRequest.getTicketTypes().stream().map(
//                ticketType ->
//                {
//                    TicketType ticketTypeToCreate = new TicketType();
//                    ticketTypeToCreate.setName(ticketType.getName());
//                    ticketTypeToCreate.setPrice(ticketType.getPrice());
//                    ticketTypeToCreate.setDescription(ticketType.getDescription());
//                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
//                    return ticketTypeToCreate;
//                }
//        ).toList();

        Event event = eventMapper.toEntity(eventRequest); // at this point, the ticket types created by MapStruct never had: ticket.setEvent(event);

        System.out.println("ticketTypes = " + event.getTicketTypes());

        event.setOrganizer(organizer);

//        List<TicketType> ticketTypes = ticketMapper.toEntities(eventRequest.getTicketTypes());

        event.addTicketTypes(
                ticketMapper.toEntities(eventRequest.getTicketTypes()));


        /* toEntity
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
        event.setTicketTypes(ticketTypesToCreate);

         */

        return eventRepository.save(event);
    }

    private User findOrganizerId(UUID organizerId) {

        return userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %s not found", organizerId)
                ));
    }
}
