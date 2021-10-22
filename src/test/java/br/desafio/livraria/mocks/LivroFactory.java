package br.desafio.livraria.mocks;

import org.modelmapper.ModelMapper;

import br.desafio.livraria.dto.request.LivroFormDto;
import br.desafio.livraria.dto.request.LivroUpdateFormDto;
import br.desafio.livraria.dto.response.LivroDto;

import br.desafio.livraria.modelo.Livro;


import java.time.LocalDate;
public class LivroFactory {
	
	private static ModelMapper modelMapper = new ModelMapper();

	public static Livro criarLivro() {

		return new Livro(1L, "Engenharia de Software", LocalDate.now(), 10, 
				AutorFactory.criarAutor());
	}

	public static Livro criarLivroSemId() {
		return new Livro(null, "Engenharia de Software", LocalDate.now(), 10, 
				AutorFactory.criarAutor());
	}

	
	public static LivroFormDto criarLivroFormDto() {
		return modelMapper.map(criarLivro(), LivroFormDto.class);
	}

	public static LivroDto criarLivroResponseDto() {
		return modelMapper.map(criarLivro(), LivroDto.class);
	}

	public static LivroUpdateFormDto criarLivroUpdateFormDto() {
		return LivroUpdateFormDto.builder().id(1L).dataDeLancamento(LocalDate.now()).numeroPaginas(10).build();
	}
	
	
    public static LivroUpdateFormDto criarLivroUpdateFormDtoComIdInvalido() {
        return LivroUpdateFormDto.builder().id(200L).build();
    }

}
