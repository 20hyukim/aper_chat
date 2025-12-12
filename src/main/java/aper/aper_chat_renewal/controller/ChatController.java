package aper.aper_chat_renewal.controller;

import aper.aper_chat_renewal.dto.request.CreateChatRoomRequest;
import aper.aper_chat_renewal.dto.response.ChatRoomResponse;
import aper.aper_chat_renewal.dto.response.CreatedChatRoomResponse;
import aper.aper_chat_renewal.service.ChatRoomService;
import com.aper.submodule.gateway.annotation.CurrentUser;
import com.aper.submodule.gateway.dto.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/rooms")
    public ResponseEntity<CreatedChatRoomResponse> createChatRoom(
            @RequestBody @Valid CreateChatRoomRequest request,
            @CurrentUser UserInfo userInfo) {
        CreatedChatRoomResponse response = chatRoomService.createChatRoom(request, userInfo.getUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms(@CurrentUser UserInfo userInfo) {
        List<ChatRoomResponse> responses = chatRoomService.getChatRoomsForUser(userInfo.getUserId());
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/rooms/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom( @PathVariable("chatRoomId") Long chatRoomId ) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.noContent().build();
    }

}
