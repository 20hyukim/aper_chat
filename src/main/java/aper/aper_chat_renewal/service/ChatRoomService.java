package aper.aper_chat_renewal.service;

import aper.aper_chat_renewal.domain.factory.ChatRoomFactory;
import aper.aper_chat_renewal.domain.policy.ChatRoomPolicy;
import aper.aper_chat_renewal.domain.policy.UserPolicy;
import aper.aper_chat_renewal.domain.query.ChatRoomQueryHelper;
import aper.aper_chat_renewal.domain.query.ChatRoomQueryResult;
import aper.aper_chat_renewal.dto.request.CreateChatRoomRequest;
import aper.aper_chat_renewal.dto.response.ChatRoomResponse;
import aper.aper_chat_renewal.dto.response.CreatedChatRoomResponse;
import aper.aper_chat_renewal.exception.BusinessException;
import aper.aper_chat_renewal.exception.ErrorCode;
import aper.aper_chat_renewal.repository.*;
import aper.aper_chat_renewal.service.mapper.ChatRoomResponseMapper;
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

    private final ChatRoomQueryHelper queryHelper;
    private final UserPolicy userPolicy;
    private final ChatRoomFactory chatRoomFactory;
    private final ChatRoomResponseMapper responseMapper;
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
    public List<ChatRoomResponse> getChatRoomsForUser(Long userId) {
        userPolicy.validateUserExists(userId);

        ChatRoomQueryResult queryResult = queryHelper.fetchChatRoomData(userId);

        return responseMapper.toResponses(queryResult);
    }


    // 채팅방의 마지막 시간 업데이트
    @Transactional
    public void updateLastMessageAt(Long chatRoomId, LocalDateTime messageTime) {
        chatRoomRepository.updateLastMessageAt(chatRoomId, messageTime);
    }
}
