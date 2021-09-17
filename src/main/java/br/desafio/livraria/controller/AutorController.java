package br.desafio.livraria.controller;


import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.livraria.dto.request.AutorDto;
import br.desafio.livraria.dto.response.MessageResponseDto;
import br.desafio.livraria.exception.AutorNotFoundException;
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
	   @DeleteMapping("/{id}")
	    @ResponseStatus(HttpStatus.NO_CONTENT)
	    public void deleteById(@PathVariable Long id) throws AutorNotFoundException {
	        autorService.delete(id);
	    }
	
	 @PostMapping
	 @ResponseStatus(HttpStatus.CREATED)
	 public MessageResponseDto createPerson(@RequestBody @Valid AutorDto AutorDto) {
	        return autorService.createAutor(AutorDto);
	    }
	 
	 
	  @GetMapping("/{id}")
	    public AutorDto findById(@PathVariable Long id) throws AutorNotFoundException {
	        return autorService.findById(id);
	    }
	  @PutMapping("/{id}")
	    public MessageResponseDto updateById(@PathVariable Long id, @RequestBody @Valid  AutorDto autorDto) throws AutorNotFoundException {
	        return autorService.updateById(id, autorDto);
	    }
}
