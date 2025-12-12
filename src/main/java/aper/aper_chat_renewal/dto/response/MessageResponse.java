package aper.aper_chat_renewal.dto.response;

import aper.aper_chat_renewal.entity.constant.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageResponse {
    private Long messageId;
    private String content;
    private MessageType type;
    private SenderInfo sender;
    private LocalDateTime createAt;
    private Integer unreadCount;

    @Getter
    @Builder
    public static class SenderInfo {
        private Long userId;
        private String nickname;
        private String profileImage;
    }
}
