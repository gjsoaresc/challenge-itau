package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Deve atualizar um produto de seguro existente"
    request {
        method 'PUT'
        urlPath("/api/produtos/670b628d-a5a8-486f-80ed-aea8ac91fd81")
        body([
                nome: "Seguro de Vida Atualizado",
                categoria: "VIDA",
                precoBase: 120.00
        ])
        headers {
            contentType('application/json')
        }
    }
    response {
        status 200
        body([
                id: $(regex(uuid())),
                nome: "Seguro de Vida Atualizado",
                categoria: "VIDA",
                precoBase: 120.00,
                precoTarifado: 123.84
        ])
        headers {
            contentType('application/json')
        }
    }
}
