
# API de Seguros

## Introdução

Este projeto é uma API RESTful para gerenciar produtos de seguros, permitindo operações como criar, atualizar e recuperar produtos de seguros, como "seguro de vida" e "seguro de automóvel". A API foi desenvolvida utilizando **Spring Boot** com **MongoDB** como banco de dados. A documentação da API é gerada automaticamente com o **Swagger**, facilitando a exploração e testes dos endpoints.

As principais operações disponíveis na API incluem:
- Criação de novos produtos de seguro.
- Recuperação de detalhes de produtos existentes.
- Atualização de produtos.

Este projeto foi desenvolvido aplicando as melhores práticas de arquitetura e desenvolvimento, como Clean Architecture e princípios SOLID.

## Instalação e Execução do Projeto

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas:
- **Java 17** ou superior
- **Gradle** (ou utilize o wrapper `./gradlew`)
- **Docker** e **Docker Compose** para rodar o MongoDB

### Instalar Dependências

Para instalar as dependências do projeto, execute o seguinte comando no diretório raiz:

```bash
./gradlew build
```

### Configuração do Docker Compose

O projeto utiliza o **MongoDB** em um container Docker. Para rodar o MongoDB, execute o seguinte comando:

```bash
docker-compose up -d
```

### Executar a Aplicação

Para iniciar a aplicação, execute o seguinte comando:

```bash
./gradlew bootRun
```

A aplicação estará disponível em `http://localhost:8080`. A documentação da API pode ser acessada em `http://localhost:8080/swagger-ui.html`.

### Executar os Testes

#### Testes Unitários e de Integração

Para executar os testes unitários e de integração, use o seguinte comando:

```bash
./gradlew test
```

#### Testes de Contrato

Os testes de contrato verificam se a API cumpre os contratos previamente estabelecidos entre os serviços. Para executar os testes de contrato, utilize o seguinte comando:

```bash
./gradlew contractTest
```

Esse comando irá verificar se a API respeita as definições dos contratos (como comunicação com outros serviços ou front-end) usando **Spring Cloud Contract**.

#### Testes End-to-End (E2E)

Os testes E2E garantem que o fluxo completo da aplicação funciona conforme o esperado. Para rodar os testes E2E, use o comando:

```bash
./gradlew e2eTest
```

Esse comando irá executar os testes E2E configurados para simular o comportamento da aplicação como um todo, incluindo o MongoDB.

## Observabilidade

A aplicação foi configurada com opções de observabilidade, incluindo monitoramento de saúde, métricas e rastreamento distribuído.

### 1. Logs Detalhados
   Os logs foram otimizados para fornecer informações claras sobre operações de criação, atualização, e busca de produtos. Cada operação registra contextos importantes, como o ID do produto, nome, categoria, e preço tarifado.

Você pode visualizar os logs da aplicação diretamente no console ou usando:

```bash
docker logs -f seguros
```

### 2. Monitoramento de Saúde (Health Checks)
   Foram implementados health checks customizados usando CustomHealthIndicator para monitorar a saúde do MongoDB e de outros componentes críticos da aplicação. Você pode acessar a URL /actuator/health para verificar o status da aplicação:

Endpoint: http://localhost:8080/actuator/health

### 3. Métricas com Prometheus
   A integração com Micrometer permite coletar métricas da aplicação. O Prometheus está configurado para capturar essas métricas. As métricas podem ser acessadas no endpoint /actuator/prometheus:

Endpoint de métricas: http://localhost:8080/actuator/prometheus

### 5. Documentação da API (Swagger/OpenAPI)
   A documentação da API é gerada automaticamente com Swagger. Acesse a documentação interativa no seguinte link:

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Scripts de Execução

- **Iniciar a aplicação**: `./gradlew bootRun`
- **Rodar testes unitários e de integração**: `./gradlew test`
- **Rodar testes de contrato**: `./gradlew contractTest`
- **Rodar testes E2E**: `./gradlew e2eTest`
- **Subir o MongoDB com Docker Compose**: `docker-compose up -d`

## Boas Práticas Aplicadas

O projeto segue várias boas práticas para garantir escalabilidade, manutenção e confiabilidade:

1. **Princípios SOLID**:
   - Cada classe e método tem uma responsabilidade única.
   - A injeção de dependência é utilizada em todo o projeto, garantindo o baixo acoplamento entre os componentes.

2. **Clean Architecture**:
   - O projeto segue uma arquitetura limpa, separando claramente as responsabilidades entre controladores, serviços e repositórios.
   - A lógica de negócios é mantida nas classes de serviço, enquanto o gerenciamento de dados ocorre na camada de repositórios.

3. **Documentação com Swagger (OpenAPI)**:
   - A API é completamente documentada usando anotações do **Springdoc OpenAPI** para fornecer descrições detalhadas de cada endpoint, incluindo a estrutura das requisições e respostas.
   - A documentação da API está disponível em `/swagger-ui.html` após a execução da aplicação.

4. **Testes Automatizados**:
   - Testes unitários e de integração garantem a correta funcionalidade da aplicação.
   - Testes de contrato garantem que a API respeite o contrato definido para a comunicação entre serviços.
   - Testes E2E verificam o comportamento da aplicação como um todo.

## Considerações Finais

Este projeto foi desenvolvido com foco em boas práticas de desenvolvimento e teste. Em um projeto real, além dos testes unitários e de integração, eu utilizaria **Cypress** para realizar testes End-to-End (E2E), garantindo que todos os fluxos da aplicação sejam testados em um ambiente próximo ao de produção. Além disso, o projeto seria codificado em inglês para facilitar a colaboração internacional e a padronização de nomenclatura no código.
