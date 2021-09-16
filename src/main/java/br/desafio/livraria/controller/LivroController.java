package br.desafio.livraria.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import br.desafio.livraria.dto.request.LivroDto;
import br.desafio.livraria.dto.response.MessageResponseDto;
import br.desafio.livraria.exception.LivroNotFoundException;
import br.desafio.livraria.service.LivroService;



@RestController
@RequestMapping("/livros")
public class LivroController {
	ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private LivroService livroService;
	 
	@GetMapping
	public List<LivroDto> listar()
	{ 
		return livroService.listAll();
	
	}
	
	 @PostMapping
	 @ResponseStatus(HttpStatus.CREATED)
	 public MessageResponseDto createLivro(@RequestBody @Valid LivroDto livroDto) {
	        return livroService.createLivro(livroDto);
	    }
	 
	 
	  @GetMapping("/{id}")
	    public LivroDto findById(@PathVariable Long id) throws LivroNotFoundException {
	        return livroService.findById(id);
	    }
	  @PutMapping("/{id}")
	    public MessageResponseDto updateById(@PathVariable Long id, @RequestBody @Valid  LivroDto livroDto) throws LivroNotFoundException {
	        return livroService.updateById(id, livroDto);
	    }
}
