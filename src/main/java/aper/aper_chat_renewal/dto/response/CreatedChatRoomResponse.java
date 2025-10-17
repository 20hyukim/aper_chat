package aper.aper_chat_renewal.dto.response;

import com.aperlibrary.chat.constant.ChatRoomType;
import com.aperlibrary.chat.entity.ChatRoom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreatedChatRoomResponse {
    private Long id;
    private String roomId;
    private String name;
    private ChatRoomType type;
    private Integer memberCount;
    private LocalDateTime createdAt;

    @Builder(access = AccessLevel.PRIVATE)
    private CreatedChatRoomResponse(Long id, String roomId, String name, ChatRoomType type, Integer memberCount, LocalDateTime createdAt) {
        this.id = id;
        this.roomId = roomId;
        this.name = name;
        this.type = type;
        this.memberCount = memberCount;
        this.createdAt = createdAt;
    }

    public static CreatedChatRoomResponse from(ChatRoom chatRoom) {
        return CreatedChatRoomResponse.builder()
                .id(chatRoom.getId())
                .roomId(chatRoom.getRoomId())
                .name(chatRoom.getName())
                .type(chatRoom.getType())
                .memberCount(chatRoom.getMemberCount())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}