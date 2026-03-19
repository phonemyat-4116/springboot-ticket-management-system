package com.phone_myat.ticketapp.exceptions;

public class EventTicketException extends RuntimeException {

    public EventTicketException() {
    }

    public EventTicketException(String message) {
        super(message);
    }

    public EventTicketException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventTicketException(Throwable cause) {
        super(cause);
    }

    public EventTicketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

/*
The reason base exception class is there.

- Groups all custom exceptions
- Enables global handling
- Improves maintainability
- Supports future extension
 */