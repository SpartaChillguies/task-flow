package min.taskflow.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamCreateRequest(
        @NotBlank(message = "팀 이름은 필수입니다.")
        @Size(max = 50, message = "팀 이름은 50자를 넘을 수 없습니다.")
        String name,

        @Size(max = 255, message = "팀 설명은 255자를 넘을 수 없습니다.")
        String description
) {}

