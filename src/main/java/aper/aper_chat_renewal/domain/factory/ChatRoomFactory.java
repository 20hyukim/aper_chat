package aper.aper_chat_renewal.domain.factory;

import aper.aper_chat_renewal.domain.policy.ChatRoomPolicy;
import aper.aper_chat_renewal.dto.request.CreateChatRoomRequest;
import aper.aper_chat_renewal.entity.ChatRoom;
import aper.aper_chat_renewal.entity.constant.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatRoomFactory {

    private final ChatRoomPolicy chatRoomPolicy;

    public ChatRoom create(CreateChatRoomRequest request) {
        chatRoomPolicy.validateRoomName(request.getName());

        int memberCount = request.getMemberIds().size() + 1;
        chatRoomPolicy.validateMemberCount(memberCount);

        return ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .name(request.getName())
                .type(ChatRoomType.GROUP)
                .memberCount(memberCount)
                .build();
    }
}
