package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Deve criar um produto de seguro"
    request {
        method 'POST'
        url '/api/produtos'
        body([
                nome: "Seguro de Vida",
                categoria: "VIDA",
                precoBase: 100.00
        ])
        headers {
            contentType('application/json')
        }
    }
    response {
        status 200
        body([
                id: $(regex(uuid())),
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
