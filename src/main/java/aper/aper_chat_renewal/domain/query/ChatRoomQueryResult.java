package aper.aper_chat_renewal.domain.query;

import com.aperlibrary.chat.entity.ChatRoom;
import com.aperlibrary.chat.entity.Message;
import com.aperlibrary.chat.entity.UserReadTracking;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

// 조회된 채팅방 목록과 관련 데이터(최근 메시지, 읽음 추적 정보)를 함께
@Getter
@AllArgsConstructor
public class ChatRoomQueryResult {
    private final List<ChatRoom> chatRooms;
    private final Map<Long, Message> latestMessageMap;
    private final Map<Long, UserReadTracking> readTrackingMap;

    public static ChatRoomQueryResult empty() {
        return new ChatRoomQueryResult(
                Collections.emptyList(),
                Collections.emptyMap(),
                Collections.emptyMap()
        );
    }

    public boolean isEmpty() {
        return chatRooms.isEmpty();
    }
}