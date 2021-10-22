package br.desafio.livraria.service;

import java.time.LocalDate;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.desafio.livraria.dto.request.LivroFormDto;
import br.desafio.livraria.dto.request.LivroUpdateFormDto;
import br.desafio.livraria.dto.response.LivroDetalhadoDto;
import br.desafio.livraria.dto.response.LivroDto;
import br.desafio.livraria.exception.DomainException;
import br.desafio.livraria.exception.ResourceNotFoundException;
import br.desafio.livraria.modelo.Livro;
import br.desafio.livraria.repository.AutorRepository;
import br.desafio.livraria.repository.LivroRepository;

@Service
public class LivroService {

	@Autowired
	private LivroRepository livroRepository;
	@Autowired
	private AutorRepository autorRepository;

	ModelMapper modelMapper = new ModelMapper();

	public Page<LivroDto> listAll(Pageable paginacao) {
		Page<Livro> allLivros = livroRepository.findAll(paginacao);
		return allLivros.map(t -> modelMapper.map(t, LivroDto.class));

	}

	public LivroDto createLivro(LivroFormDto livroFormDto) {

		Livro livroToSave = modelMapper.map(livroFormDto, Livro.class);
		livroToSave.setId(null);
		livroToSave.setAutor(autorRepository.getById(livroFormDto.getAutorId()));

		Livro savedLivro = livroRepository.save(livroToSave);
		return modelMapper.map(savedLivro, LivroDto.class);
	}

	public LivroDetalhadoDto findById(Long id) {
		Livro Livro = verifyIfExists(id);

		return modelMapper.map(Livro, LivroDetalhadoDto.class);
	}

	public void delete(Long id) {
	try {
		
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

	public LivroDto update(LivroUpdateFormDto livroUpdateFormDto) {
		try {
			var livro = livroRepository.getById(livroUpdateFormDto.getId());
			
			livro.atualizarInformacoes(livroUpdateFormDto.getTitulo(), livroUpdateFormDto.getDataDeLancamento(),
					livroUpdateFormDto.getNumeroPaginas());
			livroRepository.save(livro);
			return modelMapper.map(livro, LivroDto.class);
	
		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException("Livro inexistente: " + livroUpdateFormDto.getId());
		}
	}

}