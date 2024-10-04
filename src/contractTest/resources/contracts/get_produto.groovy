package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Deve retornar um produto existente pelo ID"
    request {
        method 'GET'
        urlPath("/api/produtos/670b628d-a5a8-486f-80ed-aea8ac91fd81")
        headers {
            contentType('application/json')
        }
    }
    response {
        status 200
        body([
                id: "670b628d-a5a8-486f-80ed-aea8ac91fd81",
                nome: $(regex('Seguro de Vida( Atualizado)?')),
                categoria: "VIDA",
                precoBase: 100.00,
                precoTarifado: 103.20
        ])
        headers {
            contentType('application/json')
        }
    }
}
