package aper.aper_chat_renewal.repository;

import aper.aper_chat_renewal.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember,Long> {
}
