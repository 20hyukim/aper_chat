package aper.aper_chat_renewal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id // TODO: should not use GeneratedValue
    private Long userId;

    private String email;
    private String penName;
    private String fieldImage;

    @OneToMany(mappedBy = "user")
    private List<ChatRoomMember> chatRoomMembers;

    @OneToMany(mappedBy = "user")
    private List<UserReadTracking> readTrackings;
}
