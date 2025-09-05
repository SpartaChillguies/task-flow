package min.taskflow.team.entity;

import jakarta.persistence.*;
import lombok.*;
import min.taskflow.common.entity.BaseEntity;
import min.taskflow.user.entity.User;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team")
public class Team extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> members = new ArrayList<>();

    @Builder
    private Team(String name, String description, List<User> members) {
        this.name = name;
        this.description = description;
        if (members != null) {
            this.members = members;
        }
    }

    public void updateTeam(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
