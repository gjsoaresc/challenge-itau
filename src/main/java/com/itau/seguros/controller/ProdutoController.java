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
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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

        if (result.hasErrors()) {
            logger.warn("Erros de validação encontrados na criação do produto: {}",
                    result.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray());
            return ResponseEntity.badRequest().body(result.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray());
        }

        Produto produto = new Produto();
        return getResponseEntity(produtoRequestDTO, produto);
    }

    @NotNull
    private ResponseEntity<?> getResponseEntity(@RequestBody @Valid ProdutoRequestDTO produtoRequestDTO, Produto produto) {
        produto.setNome(produtoRequestDTO.getNome());
        produto.setCategoria(produtoRequestDTO.getCategoria());
        produto.setPrecoBase(produtoRequestDTO.getPrecoBase());

        try {
            logger.info("Salvando produto: Nome={}, Categoria={}, Preço Base={}",
                    produto.getNome(), produto.getCategoria(), produto.getPrecoBase());

            Produto produtoSalvo = produtoService.salvarProduto(produto);

            logger.info("Produto salvo com sucesso: ID={}, Nome={}, Preço Tarifado={}",
                    produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getPrecoTarifado());

            ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(
                    produtoSalvo.getId(),
                    produtoSalvo.getNome(),
                    produtoSalvo.getCategoria(),
                    produtoSalvo.getPrecoBase(),
                    produtoSalvo.getPrecoTarifado()
            );

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Erro ao salvar o produto: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Busca um produto pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarProduto(
            @PathVariable @Parameter(description = "ID do produto a ser buscado", example = "670b628d-a5a8-486f-80ed-aea8ac91fd81") UUID id
    ) {
        logger.info("Recebida solicitação de busca do produto com ID={}", id);

        Optional<Produto> produtoOpt = produtoService.buscarProdutoPorId(id);
        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            logger.info("Produto encontrado: ID={}, Nome={}, Categoria={}",
                    produto.getId(), produto.getNome(), produto.getCategoria());

            ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(
                    produto.getId(),
                    produto.getNome(),
                    produto.getCategoria(),
                    produto.getPrecoBase(),
                    produto.getPrecoTarifado()
            );
            return ResponseEntity.ok(responseDTO);
        }
        logger.warn("Produto com ID={} não encontrado", id);
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Atualiza um produto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(
            @PathVariable @Parameter(description = "ID do produto a ser atualizado") UUID id,
            @Valid @RequestBody @Parameter(description = "Novos dados para o produto") ProdutoRequestDTO produtoRequestDTO, BindingResult result
    ) {
        logger.info("Recebida solicitação de atualização do produto com ID={}", id);

        if (result.hasErrors()) {
            logger.warn("Erros de validação encontrados na atualização do produto: {}",
                    result.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray());
            return ResponseEntity.badRequest().body(result.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray());
        }

        Optional<Produto> produtoExistente = produtoService.buscarProdutoPorId(id);
        if (produtoExistente.isEmpty()) {
            logger.warn("Produto com ID={} não encontrado para atualização", id);
            return ResponseEntity.notFound().build();
        }

        Produto produtoAtualizado = produtoExistente.get();
        logger.info("Atualizando produto: ID={}, Nome={}, Categoria={}",
                produtoAtualizado.getId(), produtoRequestDTO.getNome(), produtoRequestDTO.getCategoria());

        return getResponseEntity(produtoRequestDTO, produtoAtualizado);
    }
}
