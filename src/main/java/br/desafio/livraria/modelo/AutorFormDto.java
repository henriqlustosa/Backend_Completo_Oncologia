package br.desafio.livraria.modelo;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorFormDto {
	
	
	@NotBlank
	private String nome;
	@NotBlank
	private String email;
	@PastOrPresent
	private LocalDate dataNascimento;
	@NotBlank
	private String miniCurriculo;

}
