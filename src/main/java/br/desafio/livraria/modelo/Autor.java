package br.desafio.livraria.modelo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = { "dataNascimento", "miniCurriculo"})
@AllArgsConstructor
@NoArgsConstructor
public class Autor {
	private String nome;
	private String email;
	private LocalDate dataNascimento;
	private String miniCurriculo;

}
