package aper.aper_chat_renewal.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomListResponse {
    private Long roomId;
    private String roomName;
    private String roomImage;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Integer unreadCount;
    private Integer memberCount;
}
