package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_character")
public class GameCharacter extends CommonEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "character_id")
    private UUID id;

    @NotNull
    @Column(length=100)
    private String name;

    private String description;

    @NotNull
    @Column(length=10)
    private String grade;

    @NotNull
    private String imageUrl;

    @NotNull
    private int sellPrice;

    @OneToMany(mappedBy = "gameCharacter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CharacterInventory> characterInventories = new ArrayList<>();
}