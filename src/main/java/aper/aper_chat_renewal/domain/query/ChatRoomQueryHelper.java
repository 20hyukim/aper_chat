package aper.aper_chat_renewal.domain.query;

import aper.aper_chat_renewal.repository.ChatRoomRepository;
import aper.aper_chat_renewal.repository.MessageRepository;
import aper.aper_chat_renewal.repository.UserReadTrackingRepository;
import com.aperlibrary.chat.entity.ChatRoom;
import com.aperlibrary.chat.entity.Message;
import com.aperlibrary.chat.entity.UserReadTracking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatRoomQueryHelper {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserReadTrackingRepository userReadTrackingRepository;

    public ChatRoomQueryResult fetchChatRoomData(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberUserId(userId);

        if (chatRooms.isEmpty()) {
            return ChatRoomQueryResult.empty();
        }

        List<Long> chatRoomIds = extractChatRoomIds(chatRooms);

        Map<Long, Message> latestMessageMap = fetchLatestMessages(chatRoomIds);
        Map<Long, UserReadTracking> readTrackingMap = fetchReadTrackings(userId, chatRoomIds);

        return new ChatRoomQueryResult(chatRooms, latestMessageMap, readTrackingMap);
    }

    private List<Long> extractChatRoomIds(List<ChatRoom> chatRooms) {
        return chatRooms.stream()
                .map(ChatRoom::getId)
                .collect(Collectors.toList());
    }

    private Map<Long, Message> fetchLatestMessages(List<Long> chatRoomIds) {
        List<Message> messages = messageRepository.findLatestMessagesByChatRoomIds(chatRoomIds);

        return messages.stream()
                .collect(Collectors.toMap(
                        m -> m.getChatRoom().getId(),
                        m -> m,
                        (existing, replacement) -> existing
                ));
    }

    private Map<Long, UserReadTracking> fetchReadTrackings(Long userId, List<Long> chatRoomIds) {
        List<UserReadTracking> trackings =
                userReadTrackingRepository.findByUserIdAndChatRoomIdIn(userId, chatRoomIds);

        return trackings.stream()
                .collect(Collectors.toMap(
                        rt -> rt.getChatRoom().getId(),
                        rt -> rt,
                        (existing, replacement) -> existing
                ));
    }
}
