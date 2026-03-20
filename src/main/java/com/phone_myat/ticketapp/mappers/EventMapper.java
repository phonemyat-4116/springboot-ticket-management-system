package com.phone_myat.ticketapp.mappers;

import com.phone_myat.ticketapp.domain.CreateEventRequest;
import com.phone_myat.ticketapp.domain.CreateTicketTypeRequest;
import com.phone_myat.ticketapp.domain.dtos.CreateEventRequestDto;
import com.phone_myat.ticketapp.domain.dtos.CreateEventResponseDto;
import com.phone_myat.ticketapp.domain.dtos.CreateTicketTypeRequestDto;
import com.phone_myat.ticketapp.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);
    CreateEventRequest fromDto(CreateEventRequestDto dto);
    CreateEventResponseDto toDto(Event event);

}
