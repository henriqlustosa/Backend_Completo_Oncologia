package br.desafio.livraria.controller;


import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.livraria.dto.request.AutorDto;
import br.desafio.livraria.dto.response.MessageResponseDto;


import br.desafio.livraria.service.AutorService;




@RestController
@RequestMapping("/autores")

public class AutorController {
	

	
	ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private AutorService autorService;
	 
	@GetMapping
	public List<AutorDto> listar()
	{ 
		return autorService.listAll();
	
	}
	
	 @PostMapping
	 @ResponseStatus(HttpStatus.CREATED)
	 public MessageResponseDto createPerson(@RequestBody @Valid AutorDto AutorDto) {
	        return autorService.createAutor(AutorDto);
	    }
}
