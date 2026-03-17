package com.bank.gugu.user.model;

import com.bank.gugu.global.entity.BaseEntity;
import com.bank.gugu.user.vo.Password;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "last_visit")
    private LocalDateTime lastVisit;

    public void updatePassword(Password password) {
        this.password = password.getValue();
    }

    public void updateInfo(User user) {
        if (user.hasEmail()) {
            this.email = user.getEmail();
        }
    }

    public void updateLastVisit() {
        this.lastVisit = LocalDateTime.now();
    }

    public boolean hasEmail() {
        return this.email != null;
    }

}
