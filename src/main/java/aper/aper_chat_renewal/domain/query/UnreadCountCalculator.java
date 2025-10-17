package aper.aper_chat_renewal.domain.query;

import aper.aper_chat_renewal.repository.MessageRepository;
import com.aperlibrary.chat.entity.Message;
import com.aperlibrary.chat.entity.UserReadTracking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 읽지 않은 메시지 수 계산기 컴포넌트 , 특정 채팅방에 대해 사용자의 읽음 추적 정보와 마지막 메시지를 기반으로
@Component
@RequiredArgsConstructor
public class UnreadCountCalculator {
    private final MessageRepository messageRepository;

    public Integer calculate(Long chatRoomId,
                             UserReadTracking readTracking,
                             Message lastMessage) {
        // 읽음 추적 정보가 없으면 모든 메시지를 읽지 않은 것으로 간주
        if (readTracking == null) {
            return messageRepository.countByChatRoomId(chatRoomId);
        }

        // 최근 메시지가 없으면 읽지 않은 메시지 없음
        if (lastMessage == null) {
            return 0;
        }

        // 마지막으로 읽은 메시지가 없으면 전체 메시지 수
        if (readTracking.getLastReadMessage() == null) {
            return messageRepository.countByChatRoomId(chatRoomId);
        }

        // 마지막으로 읽은 메시지 이후의 메시지 수 계산
        Long lastReadMessageId = readTracking.getLastReadMessage().getId();
        return messageRepository.countUnreadMessages(chatRoomId, lastReadMessageId);
    }
}