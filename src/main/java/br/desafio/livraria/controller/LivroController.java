package br.desafio.livraria.controller;

import java.net.URI;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.desafio.livraria.dto.response.LivroDto;
import br.desafio.livraria.dto.request.LivroFormDto;
import br.desafio.livraria.dto.response.MessageResponseDto;
import br.desafio.livraria.exception.LivroNotFoundException;
import br.desafio.livraria.service.LivroService;

@RestController
@RequestMapping("/livros")
public class LivroController {

	@Autowired
	private LivroService livroService;

	@GetMapping
	public Page<LivroDto> listar(@PageableDefault(size = 10) Pageable paginacao) {
		return livroService.listAll(paginacao);

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Long id) throws LivroNotFoundException {
		livroService.delete(id);
	}

	@PostMapping
	public ResponseEntity<LivroDto> createLivro(@RequestBody @Valid LivroFormDto livroFormDto, UriComponentsBuilder uriBuilder) {
		
		
		LivroDto livroDto = livroService.createLivro(livroFormDto);
		
		
		URI uri = uriBuilder
				.path("/usuarios/{id}")
				.buildAndExpand(livroDto.getId())
				.toUri();
		
		return ResponseEntity.created(uri).body(livroDto);
	}

	@GetMapping("/{id}")
	public LivroFormDto findById(@PathVariable Long id) throws LivroNotFoundException {
		return livroService.findById(id);
	}

	@PutMapping("/{id}")
	public MessageResponseDto updateById(@PathVariable Long id, @RequestBody @Valid LivroFormDto livroDto)
			throws LivroNotFoundException {
		return livroService.updateById(id, livroDto);
	}
}
