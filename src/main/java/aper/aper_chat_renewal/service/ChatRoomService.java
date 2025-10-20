package aper.aper_chat_renewal.service;

import aper.aper_chat_renewal.domain.factory.ChatRoomFactory;
import aper.aper_chat_renewal.domain.policy.UserPolicy;
import aper.aper_chat_renewal.domain.query.UnreadCountCalculator;
import aper.aper_chat_renewal.dto.request.CreateChatRoomRequest;
import aper.aper_chat_renewal.dto.response.ChatRoomResponse;
import aper.aper_chat_renewal.dto.response.CreatedChatRoomResponse;
import aper.aper_chat_renewal.repository.ChatRoomMemberRepository;
import aper.aper_chat_renewal.repository.ChatRoomRepository;
import aper.aper_chat_renewal.repository.MessageRepository;
import aper.aper_chat_renewal.repository.UserReadTrackingRepository;
import com.aperlibrary.chat.entity.ChatRoom;
import com.aperlibrary.chat.entity.ChatRoomMember;
import com.aperlibrary.chat.entity.Message;
import com.aperlibrary.chat.entity.UserReadTracking;
import com.aperlibrary.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MessageRepository messageRepository;
    private final UserReadTrackingRepository userReadTrackingRepository;

    private final UnreadCountCalculator unreadCountCalculator;
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

    // 사용자의 채팅방 목록 조회 - 최근 메시지 시간순, 읽지 않은 메시지 수 포함
    // TODO: n+1 문제 생각 필요
    public List<ChatRoomResponse> getChatRoomsForUser(Long userId) {
        userPolicy.validateUserExists(userId);

        List<ChatRoom> chatRooms = chatRoomRepository.findRecentChatRooms(userId);
        List<ChatRoomResponse> chatRoomResponses = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            Message message = messageRepository.findLatestMessage(chatRoom);

            UserReadTracking tracking = userReadTrackingRepository.findByUserUserIdAndChatRoom(userId, chatRoom);
            Integer unreadCount = unreadCountCalculator.calculate(chatRoom.getId(), tracking, message);

            ChatRoomResponse chatRoomResponse = ChatRoomResponse.from(chatRoom, message, unreadCount);
            chatRoomResponses.add(chatRoomResponse);
        }

        return chatRoomResponses;
    }


    // 채팅방의 마지막 시간 업데이트
    @Transactional
    public void updateLastMessageAt(Long chatRoomId, LocalDateTime messageTime) {
        chatRoomRepository.updateLastMessageAt(chatRoomId, messageTime);
    }
}
