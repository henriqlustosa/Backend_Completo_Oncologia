package br.desafio.livraria.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AutorNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AutorNotFoundException(Long id) {

        super("Autor não encontrado com ID:" + id);
    }
}
