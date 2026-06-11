package com.phone_myat.ticketapp.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeRequestDto {

    @NotBlank(message = "Ticket type is required")
    private String name;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or greater")
    private Double price;

    private String description;
    private Integer totalAvailable;

}

/*

If a user sends a JSON request to create a ticket type but completely leaves out the totalAvailable field:
With Integer: The field will be null. Your code knows the user forgot to provide a number.
You can then use @NotNull to show a clear error message.

With int: Java cannot hold a null value in a primitive type.
It will automatically assign a default value of 0.
Your backend will mistakenly think the user explicitly wanted exactly 0 tickets available.
 */