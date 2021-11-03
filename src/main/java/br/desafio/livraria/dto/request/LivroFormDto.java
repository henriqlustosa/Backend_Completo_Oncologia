package br.desafio.livraria.dto.request;

import java.time.LocalDate;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LivroFormDto {

	

	@NotBlank
	@Size(min = 10)
	private String titulo;

	@PastOrPresent
	private LocalDate dataDeLancamento;

	
	@Min(100)
	private Integer numeroPaginas;
	

	@NotNull
    @JsonAlias("autor_id")
    private Long autorId;
	

	@NotNull
    @JsonAlias("usuario_id")
    private Long usuarioId;

}
