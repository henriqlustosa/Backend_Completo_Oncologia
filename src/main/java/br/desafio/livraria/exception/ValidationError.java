package br.desafio.livraria.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationError {
    private String campo;
    private String mensagem;

}
