package com.projects.Tasks.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity

@Table(name = "user_table")
public class User {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "id",unique = true, nullable = false)
    private UUID userId;
    @Column(name = "email",unique = true, nullable = false)
    private String email;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "username",nullable = false, unique = true)
    private String username;

    @Column(name="verified")
    private Boolean verified=false;

    @Column(name="verification_code")
    private String verificationCode;

    @Column(name="verification_code_expires_at")
    private LocalDateTime verificationCodeExpiresAt;

    @OneToMany(mappedBy = "user")
    private List<TaskList> taskLists;

    public User() {}


    public User(UUID userId, String username,String email, String password, List<TaskList> taskLists,Boolean verified) {
        this.userId = userId;
        this.username=username;
        this.email = email;
        this.password = password;
        this.taskLists = taskLists;
        this.verified=verified;
    }

    
    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getVerificationCodeExpiresAt() {
        return verificationCodeExpiresAt;
    }

    public void setVerificationCodeExpiresAt(LocalDateTime verificationCodeExpiresAt) {
        this.verificationCodeExpiresAt = verificationCodeExpiresAt;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    public void setTaskLists(List<TaskList> taskLists) {
        this.taskLists = taskLists;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                '}';
    }
}
