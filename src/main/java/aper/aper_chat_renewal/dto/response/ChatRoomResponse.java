package aper.aper_chat_renewal.dto.response;

import com.aperlibrary.chat.constant.ChatRoomType;
import com.aperlibrary.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomResponse {
    private Long id;
    private String roomId;
    private String name;
    private ChatRoomType type;
    private Integer memberCount;
    private LocalDateTime createdAt;

    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .roomId(chatRoom.getRoomId())
                .name(chatRoom.getName())
                .type(chatRoom.getType())
                .memberCount(chatRoom.getMemberCount())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}
