package springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class User implements UserDetails { // UserDetails를 상속받아 인증 객체로 사용.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;


    @Builder
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }

    @Override // 권한 반환.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override // 사용자의 패스워드를 반환
    public String getPassword() {
        return password;
    }

    @Override // 사용자의 id를 반환
    public String getUsername() {
        return email;
    }

    @Override // 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        // 만료 되었는지 확인하는 로직.
        return true; // true값이면 만료되지 않았다는 뜻.
    }

    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        // 계정이 잠금 되었는지 확인하는 로직.
        return true; // true값이면 잠금되지 않았다는 뜻.
    }

    @Override //패스워드 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true값이면 만료되지 않았다는 뜻.
    }

    @Override // 계정이 사용가능 여부 반환
    public boolean isEnabled() {
        // 계정이 사용가능 한지 확인하는 로직.
        return true; // true값이면 사용 가능 뜻.
    }
}
