package br.desafio.livraria.dto.response;

import java.time.LocalDate;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroDto {
	
	private Long id;
	private String titulo;
	private LocalDate dataDeLancamento;
	private Integer numeroPaginas;

}
