package com.itau.seguros.repository;

import com.itau.seguros.model.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProdutoRepository extends MongoRepository<Produto, UUID> {
}