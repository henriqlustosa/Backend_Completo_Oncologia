package br.desafio.livraria.service;

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.desafio.livraria.dto.request.LivroFormDto;
import br.desafio.livraria.dto.request.LivroUpdateFormDto;
import br.desafio.livraria.dto.response.LivroDetalhadoDto;
import br.desafio.livraria.dto.response.LivroDto;
import br.desafio.livraria.exception.DomainException;
import br.desafio.livraria.exception.ResourceNotFoundException;
import br.desafio.livraria.modelo.Livro;
import br.desafio.livraria.modelo.Usuario;
import br.desafio.livraria.repository.AutorRepository;
import br.desafio.livraria.repository.LivroRepository;
import br.desafio.livraria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivroService {

	@Autowired
	private LivroRepository livroRepository;
	@Autowired
	private AutorRepository autorRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public Page<LivroDto> listAll(Pageable paginacao, Usuario usuarioLogado) {
		Page<Livro> allLivros = livroRepository.findAllByUsuario(paginacao, usuarioLogado);
		return allLivros.map(t -> modelMapper.map(t, LivroDto.class));

	}

	public LivroDto createLivro(LivroFormDto livroFormDto,  Usuario usuarioLogado) {
		try {
			Usuario usuario = usuarioRepository.getById(livroFormDto.getUsuarioId());

			if (!usuario.equals(usuarioLogado)) {
				throw obterExcecaoDeAcessoNegado();
			}

		Livro livroToSave = modelMapper.map(livroFormDto, Livro.class);
		livroToSave.setId(null);
		livroToSave.setUsuario(usuarioRepository.getById(livroFormDto.getUsuarioId()));
		livroToSave.setAutor(autorRepository.getById(livroFormDto.getAutorId()));

		Livro savedLivro = livroRepository.save(livroToSave);
		return modelMapper.map(savedLivro, LivroDto.class);

		} catch (DataIntegrityViolationException e) {
			throw new DomainException("Usuario inválido");
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Usuario inválido");
		}
	}

	private AccessDeniedException obterExcecaoDeAcessoNegado() {
		return new AccessDeniedException("Acesso negado!");
	}

	@Transactional(readOnly = true)
	public LivroDetalhadoDto findById(Long id,  Usuario usuarioLogado) {
		Livro livro = verifyIfExists(id);
		if (!livro.getUsuario().equals(usuarioLogado)) {
			throw obterExcecaoDeAcessoNegado();
		}
		return modelMapper.map(livro, LivroDetalhadoDto.class);
	}

	public void delete(Long id,  Usuario usuarioLogado) {
		try {
			Livro livro = livroRepository.getById(id);
			
			if (!livro.getUsuario().equals(usuarioLogado)) {
				throw obterExcecaoDeAcessoNegado();
			}
			livroRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Autor inexistente: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DomainException("Autor não pode ser deletado");
		}
	}

	private Livro verifyIfExists(Long id) {
		return livroRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));
	}

	@Transactional
	public LivroDto update(LivroUpdateFormDto livroUpdateFormDto, Usuario usuarioLogado) {
		try {
			Livro livro = livroRepository.getById(livroUpdateFormDto.getId());
			if (!livro.getUsuario().equals(usuarioLogado)) {
				throw obterExcecaoDeAcessoNegado();
			}
			livro.atualizarInformacoes(livroUpdateFormDto.getTitulo(), livroUpdateFormDto.getDataDeLancamento(),
					livroUpdateFormDto.getNumeroPaginas());
			livroRepository.save(livro);
			return modelMapper.map(livro, LivroDto.class);

		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException("Livro inexistente: " + livroUpdateFormDto.getId());
		}
	}

}