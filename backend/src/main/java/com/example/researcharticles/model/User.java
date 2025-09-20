package com.example.researcharticles.model;

import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.researcharticles.constant.RoleName;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "users")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDocument implements UserDetails{
    @Id
    private String id;

    @Field("account")
    @Indexed(unique = true)
    @NotBlank @Size(max = 100)
    private String account;

    @Field("password")
    @NotBlank @Size(max = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Field("email")
    @Email @Size(max = 255)
    @Indexed(unique = true, sparse = true)
    private String email;

    @Field("phone")
    @Pattern(regexp = "^[+]?\\d{6,20}$", message="Số điện thoại không hợp lệ") 
    @Size(max = 15)
    @Indexed(unique = true, sparse = true)
    private String phone;

    @Field("is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Field("role")
    @Builder.Default
    private RoleName role = RoleName.USER;

    @Field("avatar_key")
    private String avatarKey; 

    @Field("avatar_url")
    private String avatarUrl;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toString()));
    }

    @Override public String getUsername() { return account; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return isActive; }
}
