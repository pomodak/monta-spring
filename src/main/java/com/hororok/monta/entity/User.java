package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @Column(columnDefinition = "BINARY(16)")
    private UUID account_id;

    @NotBlank
    @Column(length=100)
    private String nickname;

    @NotBlank @Email
    @Column(length=100)
    private String email;

    private String image_url;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Authority role;

    private long active_record_id;

    private long active_egg_id;

    @NotBlank
    private int point;

}