package br.desafio.livraria.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;



@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LivroUpdateFormDto extends LivroFormDto  {
	
	@NotNull
	private Long id;

}
