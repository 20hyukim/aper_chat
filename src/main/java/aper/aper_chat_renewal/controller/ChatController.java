package aper.aper_chat_renewal.controller;

import aper.aper_chat_renewal.dto.request.CreateChatRoomRequest;
import aper.aper_chat_renewal.dto.response.ChatRoomResponse;
import aper.aper_chat_renewal.dto.response.CreatedChatRoomResponse;
import aper.aper_chat_renewal.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/rooms")
    public ResponseEntity<CreatedChatRoomResponse> createChatRoom(
            @RequestBody @Valid CreateChatRoomRequest request,
            @RequestHeader("userId") Long userId) {
        CreatedChatRoomResponse response = chatRoomService.createChatRoom(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms(
            @RequestHeader("userId") Long userId) {
        List<ChatRoomResponse> responses = chatRoomService.getChatRoomsForUser(userId);
        return ResponseEntity.ok(responses);
    }

}
