package br.desafio.livraria.exception;

public class LivroNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LivroNotFoundException(Long id) {

        super("Person not found with ID " + id);
    }
}
