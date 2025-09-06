package min.taskflow.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import min.taskflow.common.entity.BaseEntity;
import min.taskflow.team.entity.Team;
import min.taskflow.user.enums.UserRole;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;  // 유저 PK

    @Column(unique = true)
    private String userName;  // 유저 아이디라고 생각하면 될듯합니다. 이걸로 로그인합니다.

    private String password;

    @Column(unique = true)
    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)  //팀 하나에 유저 여러명 => 1 : N = 팀 : 유저
    private Team team;

    @Builder
    public User(String userName,
                String password,
                String email,
                String name,
                UserRole role,
                Team team) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
        this.team = team;
    }

    // 연관관계 편의 메서드 (수영 : team, user와의 양방향 연관관계 때문에 만들었습니다.)
    public void assignTeam(Team team) {
        this.team = team;
    }

    public void removeFromTeam() {
        this.team = null;
    }
}
