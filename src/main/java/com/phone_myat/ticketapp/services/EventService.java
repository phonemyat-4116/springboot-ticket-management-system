package com.phone_myat.ticketapp.services;

import com.phone_myat.ticketapp.domain.CreateEventRequest;
import com.phone_myat.ticketapp.domain.entities.Event;

import java.util.UUID;

public interface EventService {

    Event createEvent(UUID organizerId, CreateEventRequest eventRequest);

}
