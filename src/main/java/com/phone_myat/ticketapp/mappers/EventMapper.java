package com.phone_myat.ticketapp.mappers;

import com.phone_myat.ticketapp.domain.requests.CreateEventRequest;
import com.phone_myat.ticketapp.domain.dtos.event.CreateEventRequestDto;
import com.phone_myat.ticketapp.domain.dtos.event.CreateEventResponseDto;
import com.phone_myat.ticketapp.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

//    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    @org.mapstruct.Mapping(target = "ticketTypes", ignore = true)
    Event toEntity(CreateEventRequest request);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

}
