package aper.aper_chat_renewal.repository;

import com.aperlibrary.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
           "JOIN cr.members m " +
           "WHERE m.user.userId = :userId")
    List<ChatRoom> findAllByMemberUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE ChatRoom cr SET cr.lastMessageAt = :messageTime WHERE cr.id = :chatRoomId")
    void updateLastMessageAt(@Param("chatRoomId") Long chatRoomId, 
                            @Param("messageTime") LocalDateTime messageTime);
}
