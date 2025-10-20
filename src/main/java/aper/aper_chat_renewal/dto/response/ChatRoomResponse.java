package aper.aper_chat_renewal.dto.response;

import com.aperlibrary.chat.constant.ChatRoomType;
import com.aperlibrary.chat.entity.ChatRoom;
import com.aperlibrary.chat.entity.Message;
import com.aperlibrary.chat.entity.UserReadTracking;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomResponse {
    private Long id;
    private String roomId;
    private String name;
    private ChatRoomType type;
    private Integer memberCount;
    private LocalDateTime createdAt;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private String lastMessageSenderName;
    private Integer unreadCount;

    @Builder(access = AccessLevel.PRIVATE)
    public ChatRoomResponse(Long id, String roomId, String name, ChatRoomType type, Integer memberCount, LocalDateTime createdAt, String lastMessage, LocalDateTime lastMessageAt, String lastMessageSenderName, Integer unreadCount) {
        this.id = id;
        this.roomId = roomId;
        this.name = name;
        this.type = type;
        this.memberCount = memberCount;
        this.createdAt = createdAt;
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
        this.lastMessageSenderName = lastMessageSenderName;
        this.unreadCount = unreadCount;
    }

    public static ChatRoomResponse from(ChatRoom chatRoom, Message message, Integer unreadCount) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .roomId(chatRoom.getRoomId())
                .name(chatRoom.getName())
                .type(chatRoom.getType())
                .memberCount(chatRoom.getMemberCount())
                .createdAt(chatRoom.getCreatedAt())
                .lastMessage(message != null ? message.getContent() : "")
                .lastMessageAt(message != null ? message.getCreatedAt() : null)
                .lastMessageSenderName(message != null ? message.getSender().getPenName() : "")
                .unreadCount(unreadCount != null ? unreadCount : 0)
                .build();
    }
}
