package br.desafio.livraria.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LivrosPorAutorDto {
	   	private String autor;
	    private Long quantidadeLivros;
	    private Double percentual;
}
