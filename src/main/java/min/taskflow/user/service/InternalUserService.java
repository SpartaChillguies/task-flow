package min.taskflow.user.service;

import lombok.RequiredArgsConstructor;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.exception.UserErrorCode;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;


/*
다른 도메인에서 참조할 메서드들 모음집
 */
@Service
@RequiredArgsConstructor
public class InternalUserService {

    private final UserRepository userRepository;


    public User findByUserId(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return user;
    }

    public UserResponse toUserResponse(User user) {

        return UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}
