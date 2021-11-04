package br.desafio.livraria.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.EmptyResultDataAccessException;

import br.desafio.livraria.dto.request.LivroFormDto;
import br.desafio.livraria.dto.request.LivroUpdateFormDto;
import br.desafio.livraria.dto.response.LivroDetalhadoDto;
import br.desafio.livraria.dto.response.LivroDto;
import br.desafio.livraria.exception.ResourceNotFoundException;
import br.desafio.livraria.mocks.*;
import br.desafio.livraria.modelo.Livro;
import br.desafio.livraria.modelo.Usuario;
import br.desafio.livraria.repository.AutorRepository;
import br.desafio.livraria.repository.LivroRepository;
import br.desafio.livraria.repository.UsuarioRepository;
import lombok.var;

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
	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private ModelMapper modelMapper;
	@InjectMocks
	private LivroService livroService;
	private LivroFormDto livroFormDto = LivroFactory.criarLivroFormDto();

	private Livro livro = LivroFactory.criarLivro();
	private LivroDetalhadoDto livroDetalhado = LivroFactory.criarLivroDetalhadoDto();

	private LivroUpdateFormDto livroUpdateFormDto = LivroFactory.criarLivroUpdateFormDtoComIdInvalido();
	private LivroDto livroDto = LivroFactory.criarLivroResponseDto();
	private Usuario usuarioLogado = UsuarioFactory.criarUsuario();
	private Usuario usuario = UsuarioFactory.criarUsuario();

	@Test
	void deveriaCadastrarUmLivro() {
		Configuration configurationMock = Mockito.mock(Configuration.class);
		when(configurationMock.setMatchingStrategy(MatchingStrategies.STRICT)).thenReturn(configurationMock);

		when(modelMapper.getConfiguration()).thenReturn(configurationMock);

		when(usuarioRepository.getById(anyLong())).thenReturn(usuario);
		when(modelMapper.map(livroFormDto, Livro.class)).thenReturn(livro);
		when(modelMapper.map(livro, LivroDto.class)).thenReturn(livroDto);
		when(livroRepository.save(Mockito.any(Livro.class))).thenAnswer(i -> i.getArguments()[0]);
		when(usuarioRepository.getById(livroFormDto.getUsuarioId())).thenReturn(usuario);

		when(usuarioRepository.getById(livroFormDto.getUsuarioId())).thenReturn(usuarioLogado);

		LivroDto dto = livroService.createLivro(livroFormDto, usuarioLogado);
		assertEquals(livroFormDto.getTitulo(), dto.getTitulo());
		assertEquals(livroFormDto.getDataDeLancamento(), dto.getDataDeLancamento());
		assertEquals(livroFormDto.getNumeroPaginas(), dto.getNumeroPaginas());

		verify(autorRepository, times(1)).getById(anyLong());
		verify(livroRepository, times(1)).save(any());

	}

	@Test
	void atualizarDeveLancarResourceNotFoundQuandoLivroIdInvalido() {
		when(livroRepository.getById(anyLong())).thenThrow(EntityNotFoundException.class);

		assertThrows(ResourceNotFoundException.class, () -> livroService.update(livroUpdateFormDto, usuarioLogado));
	}

	@Test
	void findByIdDeveRetornarTransacaoQuandoIdValido() {
		when(livroRepository.findById(anyLong())).thenReturn(Optional.of(livro));
		when(modelMapper.map(livro, LivroDetalhadoDto.class)).thenReturn(livroDetalhado);
		LivroDetalhadoDto livroResponseDto = livroService.findById(1l, usuarioLogado);

		assertEquals(livroFormDto.getTitulo(), livroResponseDto.getTitulo());
		assertEquals(livroFormDto.getDataDeLancamento(), livroResponseDto.getDataDeLancamento());
		assertEquals(livroFormDto.getNumeroPaginas(), livroResponseDto.getNumeroPaginas());
		verify(livroRepository, times(1)).findById(anyLong());

	}

	@Test
	void findByIdDeveLancarResouceNotFoundQuandoIdTransacaoInvalido() {

		assertThrows(ResourceNotFoundException.class, () -> livroService.findById(10l, usuarioLogado));
	}

	@Test
	void removerDeveriaLancarResourceNotFoundQuandoIdInvalido() {
		
		doThrow(EntityNotFoundException	.class).when(livroRepository).getById(100l);

		assertThrows(ResourceNotFoundException.class, () -> livroService.delete(100L, usuarioLogado));
	}

	@Test
	void removerNaoDeveTerRetornoComIdValido() {
		var validId = 1l;
		when(livroRepository.getById(validId)).thenReturn(livro);
		livroService.delete(validId, usuarioLogado);

		verify(livroRepository, times(1)).deleteById(1l);
	}
}
