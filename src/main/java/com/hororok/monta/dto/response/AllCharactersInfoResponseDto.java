package com.hororok.monta.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllCharactersInfoResponseDto {
    private int status;
    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Data {
        private List<Character> characters;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Character {
        @JsonProperty("character_id")
        private UUID characterId;

        private String name;
        private String description;

        @JsonProperty("image_url")
        private String imageUrl;

        private String grade;

        @JsonProperty("sell_price")
        private Integer sellPrice;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;
    }
}