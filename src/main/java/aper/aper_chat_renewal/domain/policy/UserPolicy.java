package aper.aper_chat_renewal.domain.policy;

import aper.aper_chat_renewal.entity.User;
import aper.aper_chat_renewal.exception.BusinessException;
import aper.aper_chat_renewal.exception.ErrorCode;
import aper.aper_chat_renewal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPolicy {

    private final UserRepository userRepository;

    public User validateUserExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new BusinessException(ErrorCode.USER_NOT_FOUND, "User with ID " + userId + " not found."));
    }
}
