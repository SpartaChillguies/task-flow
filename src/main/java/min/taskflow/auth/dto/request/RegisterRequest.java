package min.taskflow.auth.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/*
회원가입시 request DTO
 */
public record RegisterRequest(
        @NotBlank(message = "유저이름은 공백일수 없습니다.")
        @Size(min = 6, max = 10, message = "유저이름의 길이는 6 ~ 10 입니다.")
        String username,
        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,20}$",
                message = "비밀번호는 8~20자이며, 소문자/숫자/특수기호를 모두 포함해야 합니다.")
        String password,
        @NotBlank(message = "이메일은 공백일수 없습니다.")
        @Email(message = "이메일 형식을 지켜주세요")
        String email,
        @NotBlank(message = "이름은 공백일수 없습니다.")
        String name) {

}
