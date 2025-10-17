package aper.aper_chat_renewal.domain.factory;

import aper.aper_chat_renewal.domain.policy.ChatRoomPolicy;
import aper.aper_chat_renewal.dto.request.CreateChatRoomRequest;
import com.aperlibrary.chat.constant.ChatRoomType;
import com.aperlibrary.chat.entity.ChatRoom;
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
