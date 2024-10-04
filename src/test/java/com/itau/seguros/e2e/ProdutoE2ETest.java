package com.itau.seguros.e2e;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProdutoE2ETest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private String produtoId;

    @Test
    void deveCriarEConsultarProduto() {
        String nomeProduto = "Seguro Auto " + UUID.randomUUID();
        String produtoJson = "{ \"nome\": \"" + nomeProduto + "\", \"categoria\": \"AUTO\", \"precoBase\": 200.00 }";

        produtoId = given()
                .contentType(ContentType.JSON)
                .body(produtoJson)
                .post("/api/produtos")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .get("/api/produtos/{id}", produtoId)
                .then()
                .statusCode(200)
                .body("nome", equalTo(nomeProduto))
                .body("precoTarifado", equalTo(221.00f));
    }
}
