package aper.aper_chat_renewal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WebSocketMessage {
    private String type;
    private String nickname;
    private String profileImage;
}
