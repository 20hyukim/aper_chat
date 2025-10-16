package aper.aper_chat_renewal.controller;

import aper.aper_chat_renewal.dto.request.CreateChatRoomRequest;
import aper.aper_chat_renewal.dto.response.ChatRoomResponse;
import aper.aper_chat_renewal.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomResponse> createChatRoom(
            @RequestBody @Valid CreateChatRoomRequest request,
            @RequestHeader("userId") Long userId) {
        ChatRoomResponse response = chatRoomService.createChatRoom(request, userId);
        return ResponseEntity.ok(response);
    }
}
