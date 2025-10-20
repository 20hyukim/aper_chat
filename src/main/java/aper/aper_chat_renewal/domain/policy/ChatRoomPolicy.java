package aper.aper_chat_renewal.domain.policy;

import aper.aper_chat_renewal.exception.BusinessException;
import aper.aper_chat_renewal.exception.ErrorCode;
import aper.aper_chat_renewal.repository.ChatRoomRepository;
import com.aperlibrary.chat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomPolicy {

    private final ChatRoomRepository chatRoomRepository;

    private static final int MAX_MEMBERS = 100;

    public void validateRoomName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.CHAT_ROOM_NAME_REQUIRED);
        }
    }

    public void validateMemberCount(int count) {
        if (count > MAX_MEMBERS) {
            throw new BusinessException(ErrorCode.CHAT_ROOM_MEMBER_LIMIT_EXCEEDED);
        }
    }

    public ChatRoom validateChatRoomExists(Long chatRoomId) {
        return chatRoomRepository.getChatRoomById(chatRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND)
        );
    }
}
