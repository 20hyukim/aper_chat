package aper.aper_chat_renewal.service;

import aper.aper_chat_renewal.dto.request.CreateChatRoomRequest;
import aper.aper_chat_renewal.dto.response.ChatRoomResponse;
import aper.aper_chat_renewal.exception.BusinessException;
import aper.aper_chat_renewal.exception.ErrorCode;
import aper.aper_chat_renewal.repository.ChatRoomMemberRepository;
import aper.aper_chat_renewal.repository.ChatRoomRepository;
import aper.aper_chat_renewal.repository.UserRepository;
import com.aperlibrary.chat.constant.MemberRole;
import com.aperlibrary.chat.entity.ChatRoom;
import com.aperlibrary.chat.entity.ChatRoomMember;
import com.aperlibrary.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;


    @Transactional
    public ChatRoomResponse createChatRoom(CreateChatRoomRequest request, Long userId) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND,
                                                        ErrorCode.USER_NOT_FOUND.getMessage() + " : " + userId));
        
        // Create chat room
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .name(request.getName())
                .type(request.getType())
                .memberCount(request.getMemberIds().size() + 1)
                .build();

        chatRoom = chatRoomRepository.save(chatRoom);

        // Add creator as member
        addMember(chatRoom, creator, true);

        for (Long memberId : request.getMemberIds()) {
            User member = userRepository.findById(memberId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage() + " : " + memberId));
            addMember(chatRoom, member, false);
        }

        return ChatRoomResponse.from(chatRoom);
    }

    private void addMember(ChatRoom chatRoom, User user, boolean isOwner) {
        ChatRoomMember member = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .user(user)
                .role(MemberRole.valueOf(isOwner ? "OWNER" : "MEMBER"))
                .joinedAt(LocalDateTime.now())
                .notificationEnabled(true)
                .build();

        chatRoomMemberRepository.save(member);
    }
}
