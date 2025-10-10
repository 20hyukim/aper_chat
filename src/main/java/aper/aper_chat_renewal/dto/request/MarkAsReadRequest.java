package aper.aper_chat_renewal.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MarkAsReadRequest {
    @NotNull
    private Long lastReadMessageId;
}
