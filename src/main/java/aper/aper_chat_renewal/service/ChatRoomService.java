package aper.aper_chat_renewal.service;

import aper.aper_chat_renewal.domain.factory.ChatRoomFactory;
import aper.aper_chat_renewal.domain.policy.ChatRoomPolicy;
import aper.aper_chat_renewal.domain.policy.UserPolicy;
import aper.aper_chat_renewal.dto.request.CreateChatRoomRequest;
import aper.aper_chat_renewal.dto.response.ChatRoomResponse;
import aper.aper_chat_renewal.dto.response.CreatedChatRoomResponse;
import aper.aper_chat_renewal.exception.BusinessException;
import aper.aper_chat_renewal.exception.ErrorCode;
import aper.aper_chat_renewal.repository.*;
import com.aperlibrary.chat.constant.MemberRole;
import com.aperlibrary.chat.entity.ChatRoom;
import com.aperlibrary.chat.entity.ChatRoomMember;
import com.aperlibrary.chat.entity.Message;
import com.aperlibrary.chat.entity.UserReadTracking;
import com.aperlibrary.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MessageRepository messageRepository;
    private final UserReadTrackingRepository userReadTrackingRepository;

    private final UserPolicy userPolicy;
    private final ChatRoomFactory chatRoomFactory;
    // TODO: Redis 캐시 적용

    @Transactional
    public CreatedChatRoomResponse createChatRoom(CreateChatRoomRequest request, Long userId) {
        User creator = userPolicy.validateUserExists(userId);
        ChatRoom chatRoom = chatRoomFactory.create(request);
        chatRoom = chatRoomRepository.save(chatRoom);

        addMember(chatRoom, creator, true);

        for (Long memberId : request.getMemberIds()) {
            User member = userPolicy.validateUserExists(memberId);
            addMember(chatRoom, member, false);
        }

        return CreatedChatRoomResponse.from(chatRoom);
    }

    private void addMember(ChatRoom chatRoom, User user, boolean isOwner) {
        ChatRoomMember member = ChatRoomMember.create(chatRoom, user, isOwner);
        chatRoomMemberRepository.save(member);
    }

    public List<ChatRoomResponse> getChatRoomsForUser(Long userId) {
        userPolicy.validateUserExists(userId);

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberUserId(userId);
        // TODO: 해당 부분을 더 간결하게.. 적을 순 없을까
        if (chatRooms.isEmpty()) {
            return new ArrayList<>();
        }

        // 채팅방 ID 목록 추출
        List<Long> chatRoomIds = chatRooms.stream()
                .map(ChatRoom::getId)
                .collect(Collectors.toList());

        // 각 채팅방의 최근 메시지 한번에 조회 (N+1 방지)
        List<Message> latestMessages = messageRepository.findLatestMessagesByChatRoomIds(chatRoomIds);

        Map<Long, Message> latestMessageMap = latestMessages.stream()
                .collect(Collectors.toMap(
                        m -> m.getChatRoom().getId(),
                        m -> m,
                        (existing, replacement) -> existing // 중복 시 기존 값 유지
                ));

        // 사용자의 읽음 상태 한번에 조회 (N+1 방지)
        List<UserReadTracking> readTrackings = userReadTrackingRepository.findByUserIdAndChatRoomIdIn(userId, chatRoomIds);

        Map<Long, UserReadTracking> readTrackingMap = readTrackings.stream()
                .collect(Collectors.toMap(
                        rt -> rt.getChatRoom().getId(),
                        rt -> rt,
                        (existing, replacement) -> existing
                ));


        return chatRooms.stream()
                .map(chatRoom -> {
                    Message lastMessage = latestMessageMap.get(chatRoom.getId());
                    UserReadTracking readTracking = readTrackingMap.get(chatRoom.getId());

                    Integer unreadCount = calculateUnreadCount(
                            chatRoom.getId(),
                            readTracking,
                            lastMessage);

                    return ChatRoomResponse.from(chatRoom, lastMessage, unreadCount);
                })
                .sorted((a, b) -> {
                    if (a.getLastMessageAt() == null) return 1;
                    if (b.getLastMessageAt() == null) return -1;
                    return b.getLastMessageAt().compareTo(a.getLastMessageAt());
                })
                .collect(Collectors.toList());
    }

    private Integer calculateUnreadCount(Long chatRoomId,
                                         UserReadTracking readTracking,
                                         Message lastMessage) {
        // 읽음 추적 정보가 없으면 모든 메시지를 읽지 않은 것으로 간주
        if (readTracking == null) {
            // 채팅방의 전체 메시지 수를 반환 (또는 최대값 설정)
            return messageRepository.countByChatRoomId(chatRoomId);
        }

        // 최근 메시지가 없으면 0
        if (lastMessage == null) {
            return 0;
        }

        // 마지막으로 읽은 메시지가 없으면 전체 메시지 수
        if (readTracking.getLastReadMessage() == null) {
            return messageRepository.countByChatRoomId(chatRoomId);
        }

        Long lastReadMessageId = readTracking.getLastReadMessage().getId();

        // 마지막으로 읽은 메시지 이후의 메시지 수 계산
        return messageRepository.countUnreadMessages(chatRoomId, lastReadMessageId);
    }

    // 추가: 메시지 전송 시 ChatRoom의 lastMessageAt 업데이트를 위한 메서드
    @Transactional
    public void updateLastMessageAt(Long chatRoomId, LocalDateTime messageTime) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // ChatRoom 엔티티에 setter 메서드가 필요함
        // chatRoom.updateLastMessageAt(messageTime);

        // 또는 JPQL로 직접 업데이트
        chatRoomRepository.updateLastMessageAt(chatRoomId, messageTime);
    }

}
