package com.itau.seguros.integration;

import com.itau.seguros.dto.ProdutoRequestDTO;
import com.itau.seguros.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProdutoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void deveCriarProdutoComSucesso() {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO();
        requestDTO.setNome("Seguro Auto");
        requestDTO.setCategoria("AUTO");
        requestDTO.setPrecoBase(new BigDecimal("200.00"));

        ResponseEntity<Produto> response = restTemplate.postForEntity("/api/produtos", requestDTO, Produto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Produto produtoCriado = response.getBody();
        assertThat(produtoCriado).isNotNull();
        assertThat(produtoCriado.getNome()).isEqualTo("Seguro Auto");
        assertThat(produtoCriado.getPrecoTarifado()).isGreaterThan(new BigDecimal("200.00"));
    }

    @Test
    public void deveBuscarProdutoPorId() {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO();
        requestDTO.setNome("Seguro Auto");
        requestDTO.setCategoria("AUTO");
        requestDTO.setPrecoBase(new BigDecimal("200.00"));

        ResponseEntity<Produto> postResponse = restTemplate.postForEntity("/api/produtos", requestDTO, Produto.class);
        Produto produtoCriado = postResponse.getBody();
        assert produtoCriado != null;
        UUID produtoId = produtoCriado.getId();

        ResponseEntity<Produto> getResponse = restTemplate.getForEntity("/api/produtos/{id}", Produto.class, produtoId);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Produto produtoBuscado = getResponse.getBody();
        assertThat(produtoBuscado).isNotNull();
        assertThat(produtoBuscado.getNome()).isEqualTo("Seguro Auto");
        assertThat(produtoBuscado.getPrecoTarifado()).isGreaterThan(new BigDecimal("200.00"));
    }

    @Test
    public void deveAtualizarProduto() {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO();
        requestDTO.setNome("Seguro Residencial");
        requestDTO.setCategoria("RESIDENCIAL");
        requestDTO.setPrecoBase(new BigDecimal("300.00"));

        ResponseEntity<Produto> postResponse = restTemplate.postForEntity("/api/produtos", requestDTO, Produto.class);
        Produto produtoCriado = postResponse.getBody();
        assert produtoCriado != null;
        UUID produtoId = produtoCriado.getId();

        ProdutoRequestDTO requestAtualizacao = new ProdutoRequestDTO();
        requestAtualizacao.setNome("Seguro Residencial Atualizado");
        requestAtualizacao.setCategoria("RESIDENCIAL");
        requestAtualizacao.setPrecoBase(new BigDecimal("400.00"));

        restTemplate.put("/api/produtos/{id}", requestAtualizacao, produtoId);

        ResponseEntity<Produto> getResponse = restTemplate.getForEntity("/api/produtos/{id}", Produto.class, produtoId);
        Produto produtoAtualizado = getResponse.getBody();

        assertThat(produtoAtualizado).isNotNull();
        assertThat(produtoAtualizado.getNome()).isEqualTo("Seguro Residencial Atualizado");
        assertThat(produtoAtualizado.getPrecoBase()).isEqualTo(new BigDecimal("400.00"));
    }
}
