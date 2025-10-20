package aper.aper_chat_renewal.repository;

import com.aperlibrary.chat.entity.ChatRoom;
import com.aperlibrary.chat.entity.UserReadTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReadTrackingRepository extends JpaRepository<UserReadTracking,Long> {

    // 특정 사용자의 여러 채팅방 읽음 상태 한번에 조회 (N+1 방지)
    @Query("SELECT urt FROM UserReadTracking urt " +
            "WHERE urt.user.userId = :userId " +
            "AND urt.chatRoom.id IN :chatRoomIds")
    List<UserReadTracking> findByUserIdAndChatRoomIdIn(@Param("userId") Long userId,
                                                       @Param("chatRoomIds") List<Long> chatRoomIds);

    UserReadTracking findByUserUserIdAndChatRoom(Long userId, ChatRoom chatRoom);
}
