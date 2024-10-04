package com.itau.seguros.controller;

import com.itau.seguros.dto.ProdutoRequestDTO;
import com.itau.seguros.dto.ProdutoResponseDTO;
import com.itau.seguros.model.Produto;
import com.itau.seguros.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "API para gerenciamento de produtos de seguros")
public class ProdutoController {
    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    @Autowired
    private ProdutoService produtoService;

    @Operation(summary = "Cria um novo produto de seguro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> criarProduto(
            @Valid @RequestBody @Parameter(description = "Dados para criação do produto") ProdutoRequestDTO produtoRequestDTO,
            BindingResult result
    ) {
        logger.info("Recebida solicitação de criação de produto: Nome={}, Categoria={}",
                produtoRequestDTO.getNome(), produtoRequestDTO.getCategoria());

        ResponseEntity<?> erroValidacao = validarErros(result);
        if (erroValidacao != null) return erroValidacao;

        try {
            Produto produtoSalvo = produtoService.salvarProduto(produtoRequestDTO);
            return ResponseEntity.ok(converterParaDTO(produtoSalvo));
        } catch (Exception e) {
            logger.error("Erro ao salvar o produto: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Busca um produto pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarProduto(
            @PathVariable @Parameter(description = "ID do produto a ser buscado") UUID id
    ) {
        logger.info("Recebida solicitação de busca do produto com ID={}", id);

        return produtoService.buscarProdutoPorId(id)
                .map(produto -> ResponseEntity.ok(converterParaDTO(produto)))
                .orElseGet(() -> {
                    logger.warn("Produto com ID={} não encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(summary = "Atualiza um produto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(
            @PathVariable @Parameter(description = "ID do produto a ser atualizado") UUID id,
            @Valid @RequestBody @Parameter(description = "Novos dados para o produto") ProdutoRequestDTO produtoRequestDTO,
            BindingResult result
    ) {
        logger.info("Recebida solicitação de atualização do produto com ID={}", id);

        ResponseEntity<?> erroValidacao = validarErros(result);
        if (erroValidacao != null) return erroValidacao;

        try {
            Produto produtoAtualizado = produtoService.atualizarProduto(id, produtoRequestDTO);
            return ResponseEntity.ok(converterParaDTO(produtoAtualizado));
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar o produto: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    private ResponseEntity<?> validarErros(BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    result.getFieldErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .toArray()
            );
        }
        return null;
    }

    private ProdutoResponseDTO converterParaDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getPrecoBase(),
                produto.getPrecoTarifado()
        );
    }
}
