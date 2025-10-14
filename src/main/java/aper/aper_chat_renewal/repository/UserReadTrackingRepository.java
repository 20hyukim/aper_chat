package aper.aper_chat_renewal.repository;

import com.aperlibrary.chat.entity.UserReadTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReadTrackingRepository extends JpaRepository<UserReadTracking,Long> {
}
