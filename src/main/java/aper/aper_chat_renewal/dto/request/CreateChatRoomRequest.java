package aper.aper_chat_renewal.dto.request;

import com.aperlibrary.chat.constant.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateChatRoomRequest {
    @NotBlank(message = "Room name is required")
    private String name;

    @NotNull(message = "Room type is required")
    private ChatRoomType type;

    @NotEmpty(message = "At least one member is required")
    private List<Long> memberIds;
}
