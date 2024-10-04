package com.itau.seguros.contract;

import com.itau.seguros.model.Produto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.itau.seguros.repository.ProdutoRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    private final String fixedUUID = "670b628d-a5a8-486f-80ed-aea8ac91fd81";

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        produtoRepository.deleteAll();

        Produto produto = new Produto();
        produto.setId(UUID.fromString(fixedUUID));
        produto.setNome("Seguro de Vida");
        produto.setCategoria("VIDA");
        produto.setPrecoBase(new BigDecimal("100.00"));
        produto.setPrecoTarifado(new BigDecimal("103.20"));

        produtoRepository.save(produto);
    }
}
