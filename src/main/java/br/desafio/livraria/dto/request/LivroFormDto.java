package br.desafio.livraria.dto.request;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivroFormDto {

	private Long id;

	@NotBlank
	@Size(min = 10)
	private String titulo;

	@PastOrPresent
	private LocalDate dataDeLancamento;

	
	@Min(100)
	private Integer numeroPaginas;
	
	@Valid
	@NotNull
	private AutorFormDto autor;
}
