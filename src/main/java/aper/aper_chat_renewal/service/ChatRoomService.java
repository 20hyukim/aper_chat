package aper.aper_chat_renewal.service;

import aper.aper_chat_renewal.dto.request.CreateChatRoomRequest;
import aper.aper_chat_renewal.dto.response.ChatRoomResponse;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;


    public ChatRoomResponse createChatRoom(CreateChatRoomRequest request, Long creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + creatorId));

        // Create chat room
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .name(request.getName())
                .type(request.getType())
                .memberCount(request.getMemberIds().size() + 1) // Including creator
                .build();

        chatRoom = chatRoomRepository.save(chatRoom);

        // Add creator as member
        addMember(chatRoom, creator, true);

        // Add invited members
        for (Long memberId : request.getMemberIds()) {
            User member = userRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + memberId));
            addMember(chatRoom, member, false);
        }

        return ChatRoomResponse.from(chatRoom);
    }

    private void addMember(ChatRoom chatRoom, User user, boolean isOwner) {
        ChatRoomMember member = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .user(user)
                .role(MemberRole.valueOf(isOwner ? "OWNER" : "MEMBER"))
                .build();

        chatRoomMemberRepository.save(member);
    }
}
