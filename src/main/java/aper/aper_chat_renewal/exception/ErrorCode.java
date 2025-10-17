package aper.aper_chat_renewal.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "User not found"),

    // ChatRoom
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "Chat room not found"),
    CHAT_ROOM_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "C002", "Chat room name is required"),
    CHAT_ROOM_MEMBER_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "C003", "Chat room member limit exceeded"),

    // Global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G001", "Internal server error"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "G002", "Invalid input value");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
