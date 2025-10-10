package aper.aper_chat_renewal.dto.request;

import com.aperlibrary.chat.constant.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SendMessageRequest {
    @NotBlank
    private String content;

    @NotNull
    private MessageType type;

    private String fileUrl;
}
