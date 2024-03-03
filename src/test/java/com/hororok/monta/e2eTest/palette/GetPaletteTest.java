package com.hororok.monta.e2eTest.palette;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.palette.GetPalettesResponseDto;
import com.hororok.monta.setting.TestSetting;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class GetPaletteTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> returnExtractableResponse(String role) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + TestSetting.returnToken(role))
                .when().get("/admin/palettes")
                .then().log().all().extract();
    }

    @DisplayName("성공")
    @Test
    @Transactional
    public void getPalettesByAdmin() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Admin");
        GetPalettesResponseDto response = extractableResponse.as(GetPalettesResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("success");
    }

    @DisplayName("실패 : 권한 없음")
    @Test
    @Transactional
    public void getPalettesByUser() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("User");
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(403);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("해당 권한이 없습니다.");
    }

    @DisplayName("실패 : 인증되지 않은 사용자")
    @Test
    @Transactional
    public void getPalettesByElse() {
        ExtractableResponse<Response> extractableResponse = returnExtractableResponse("Else");
        FailResponseDto response = extractableResponse.as(FailResponseDto.class);

        assertThat(extractableResponse.statusCode()).isEqualTo(401);
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).contains("인증되지 않은 사용자의 접근입니다.");
    }
}
