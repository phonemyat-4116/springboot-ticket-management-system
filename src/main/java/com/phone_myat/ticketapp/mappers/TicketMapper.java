package com.phone_myat.ticketapp.mappers;

import com.phone_myat.ticketapp.domain.requests.CreateTicketTypeRequest;
import com.phone_myat.ticketapp.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    TicketType toEntity(CreateTicketTypeRequest request);

    List<TicketType> toEntities(List<CreateTicketTypeRequest> request);

}

/*
Internally

@Override
public List<TicketType> toEntities(List<CreateTicketTypeRequest> requests) {

    if (requests == null) {
        return null;
    }

    List<TicketType> list = new ArrayList<>(requests.size());

    for (CreateTicketTypeRequest request : requests) {
        list.add(toEntity(request));
    }

    return list;
}
 */
