package br.edu.infnet.victorapi.modules.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "name",
        "email",
        "phone",
        "position",
        "departmentName",
        "role",
        "isActive",
        "lastLogin",
        "createdAt"
})
public class UserInfoDTO {

    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String position;
    private String departmentName;
    private UserRole role;
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public UserInfoDTO(Integer id, String name, String email, String phone,
                       String position, Boolean isActive, LocalDateTime lastLogin,
                       LocalDateTime createdAt, UserRole role, String departmentName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.isActive = isActive;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.role = role;
        this.departmentName = departmentName;
    }

    public UserInfoDTO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}