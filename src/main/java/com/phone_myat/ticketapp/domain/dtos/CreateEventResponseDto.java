package com.phone_myat.ticketapp.domain.dtos;

import com.phone_myat.ticketapp.domain.CreateTicketTypeRequest;
import com.phone_myat.ticketapp.domain.entities.EventStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventResponseDto {

    private UUID id;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private String venue;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;
    private EventStatusEnum status;
    private List<CreateTicketTypeResponseDto> ticketTypes = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
