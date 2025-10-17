package aper.aper_chat_renewal.service.mapper;

import aper.aper_chat_renewal.domain.query.ChatRoomQueryResult;
import aper.aper_chat_renewal.domain.query.UnreadCountCalculator;
import aper.aper_chat_renewal.dto.response.ChatRoomResponse;
import com.aperlibrary.chat.entity.ChatRoom;
import com.aperlibrary.chat.entity.Message;
import com.aperlibrary.chat.entity.UserReadTracking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Domain 객체를 Response DTO로 변환하는 Mapper
 * 채팅방 목록을 최근 메시지 시간순으로 정렬하여 응답
 */
@Component
@RequiredArgsConstructor
public class ChatRoomResponseMapper {
    private final UnreadCountCalculator unreadCountCalculator;

    public List<ChatRoomResponse> toResponses(ChatRoomQueryResult queryResult) {
        if (queryResult.isEmpty()) {
            return Collections.emptyList();
        }

        return queryResult.getChatRooms().stream()
                .map(chatRoom -> toResponse(chatRoom, queryResult))
                .sorted(new LastMessageTimeComparator())
                .collect(Collectors.toList());
    }

    private ChatRoomResponse toResponse(ChatRoom chatRoom, ChatRoomQueryResult queryResult) {
        Long chatRoomId = chatRoom.getId();
        Message lastMessage = queryResult.getLatestMessageMap().get(chatRoomId);
        UserReadTracking readTracking = queryResult.getReadTrackingMap().get(chatRoomId);

        Integer unreadCount = unreadCountCalculator.calculate(
                chatRoomId,
                readTracking,
                lastMessage
        );

        return ChatRoomResponse.from(chatRoom, lastMessage, unreadCount);
    }

    /**
     * 최근 메시지 시간 기준 내림차순 정렬
     * null 값은 마지막으로 정렬
     */
    private static class LastMessageTimeComparator implements Comparator<ChatRoomResponse> {
        @Override
        public int compare(ChatRoomResponse a, ChatRoomResponse b) {
            if (a.getLastMessageAt() == null) return 1;
            if (b.getLastMessageAt() == null) return -1;
            return b.getLastMessageAt().compareTo(a.getLastMessageAt());
        }
    }
}