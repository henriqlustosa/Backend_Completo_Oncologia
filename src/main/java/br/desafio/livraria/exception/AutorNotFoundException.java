package br.desafio.livraria.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AutorNotFoundException {

	public AutorNotFoundException(Long id) {

        super();
    }
}
