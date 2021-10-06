package br.desafio.livraria.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutorFormDto {
	private Long id;
    @NotEmpty
    @Size(min = 2, max = 100)
	private String nome;

    @NotEmpty
    @Email
	private String email;
    

    @PastOrPresent
	private LocalDate dataNascimento;
    @NotEmpty
    @Size(min = 2, max = 100)
	private String miniCurriculo;

}
