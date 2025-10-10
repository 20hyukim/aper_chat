package aper.aper_chat_renewal.dto.request;

import com.aperlibrary.chat.constant.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateChatRoomRequest {
    @NotBlank
    private String name;

    @NotNull
    private ChatRoomType type;

    @NotEmpty
    private List<String> memberIds;
}
