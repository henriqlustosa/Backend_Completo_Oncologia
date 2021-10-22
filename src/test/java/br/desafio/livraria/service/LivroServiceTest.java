package br.desafio.livraria.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import br.desafio.livraria.dto.request.LivroFormDto;
import br.desafio.livraria.dto.request.LivroUpdateFormDto;
import br.desafio.livraria.dto.response.LivroDetalhadoDto;
import br.desafio.livraria.dto.response.LivroDto;
import br.desafio.livraria.exception.ResourceNotFoundException;
import br.desafio.livraria.mocks.*;
import br.desafio.livraria.modelo.Livro;
import br.desafio.livraria.repository.AutorRepository;
import br.desafio.livraria.repository.LivroRepository;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {
	@Mock
	private LivroRepository livroRepository;
	@Mock
	private AutorRepository autorRepository;

	@InjectMocks
	private LivroService livroService;
	private LivroFormDto livroFormDto = LivroFactory.criarLivroFormDto();

	private Livro livro = LivroFactory.criarLivro();

	private LivroUpdateFormDto livroUpdateFormDto = LivroFactory.criarLivroUpdateFormDtoComIdInvalido();
	
	

	@Test
	void deveriaCadastrarUmaTransacao() {

		when(livroRepository.save(Mockito.any(Livro.class))).thenAnswer(i -> i.getArguments()[0]);
		when(autorRepository.getById(livroFormDto.getAutorId()))
				.thenReturn(AutorFactory.criarAutor());

		LivroDto dto = livroService.createLivro(livroFormDto);
		assertEquals(livroFormDto.getTitulo(), dto.getTitulo());
		assertEquals(livroFormDto.getDataDeLancamento(), dto.getDataDeLancamento());
		assertEquals(livroFormDto.getNumeroPaginas(), dto.getNumeroPaginas());

		verify(autorRepository, times(1)).getById(anyLong());
		verify(livroRepository, times(1)).save(any());

	}
	
	
	@Test
	void atualizarDeveLancarResourceNotFoundQuandoTransacaoIdInvalido() {
		when(livroRepository.getById(anyLong())).thenThrow(EntityNotFoundException.class);

		assertThrows(ResourceNotFoundException.class, () -> livroService.update(livroUpdateFormDto));
	}
	
	@Test
	void findByIdDeveRetornarTransacaoQuandoIdValido() {
		when(livroRepository.findById(anyLong())).thenReturn(Optional.of(livro));
		LivroDetalhadoDto livroResponseDto = livroService.findById(1l);

		assertEquals(livroFormDto.getTitulo(), livroResponseDto.getTitulo());
		assertEquals(livroFormDto.getDataDeLancamento(), livroResponseDto.getDataDeLancamento());
		assertEquals(livroFormDto.getNumeroPaginas(), livroResponseDto.getNumeroPaginas());
		verify(livroRepository, times(1)).findById(anyLong());
	
	} 
   
	@Test
	void findByIdDeveLancarResouceNotFoundQuandoIdTransacaoInvalido() {
		
		assertThrows(ResourceNotFoundException.class, () -> livroService.findById(10l));
	}
	

    @Test
    void removerDeveriaLancarResourceNotFoundQuandoIdInvalido() {
        doThrow(EmptyResultDataAccessException.class).when(livroRepository).deleteById(anyLong());

        assertThrows(ResourceNotFoundException.class, () -> livroService.delete(100L));
    }

	@Test
	void removerNaoDeveTerRetornoComIdValido() {
		var validId = 1l;

		livroService.delete(validId);

		verify(livroRepository, times(1)).deleteById(1l);
	}
}
