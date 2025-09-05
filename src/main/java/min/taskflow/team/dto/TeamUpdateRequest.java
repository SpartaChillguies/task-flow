package min.taskflow.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamUpdateRequest(
        @NotBlank(message = "팀 이름을 입력해주세요.")
        @Size(max = 255, message = "팀 이름은 255자를 넘을 수 없습니다.")
        String name,

        @Size(max = 500, message = "설명은 500자를 넘을 수 없습니다.")
        String description
) {}
