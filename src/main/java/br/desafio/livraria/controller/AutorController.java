package br.desafio.livraria.controller;



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

import br.desafio.livraria.dto.request.AutorFormDto;
import br.desafio.livraria.dto.response.AutorDto;



import br.desafio.livraria.service.AutorService;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
@RestController
@RequestMapping("/autores")

public class AutorController {

	@Autowired
	private AutorService autorService;

	@GetMapping
	public Page<AutorDto> listar(@PageableDefault(size = 10) Pageable paginacao) {
		return autorService.listAll(paginacao);

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void>  deleteById(@PathVariable Long id) {
		autorService.delete(id);
		 return ResponseEntity.noContent().build();
	}

	@PostMapping
	public ResponseEntity<AutorDto> createPerson(@RequestBody @Valid AutorFormDto autorFormDto, UriComponentsBuilder uriBuilder) {
		
		AutorDto autorDto= autorService.createAutor(autorFormDto);
		URI uri = uriBuilder
				.path("/autores/{id}")
				.buildAndExpand(autorDto.getId())
				.toUri();
		return ResponseEntity.created(uri).body(autorDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AutorDto> findById(@PathVariable Long id) {
		return new ResponseEntity<>(autorService.findById(id), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AutorDto>updateById(@PathVariable Long id, @RequestBody @Valid AutorFormDto autorFormDto, UriComponentsBuilder uriBuilder)
	 {
	
	
		
		return new ResponseEntity<>(autorService.updateById(id, autorFormDto), HttpStatus.OK);
		
		
	}
}
