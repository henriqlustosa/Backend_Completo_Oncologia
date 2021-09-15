package br.desafio.livraria.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.livraria.dto.AutorDto;
import br.desafio.livraria.modelo.Autor;
import br.desafio.livraria.modelo.AutorFormDto;

@RestController
@RequestMapping("/autores")
public class AutorController {
	
	private List<Autor> autores = new ArrayList<>();
	
	ModelMapper modelMapper = new ModelMapper();
	
	@GetMapping
	public List<AutorDto> listar()
	{
		return autores
				.stream()
				.map(t -> modelMapper.map(t, AutorDto.class))
				.collect(Collectors.toList());
	}
	
	@PostMapping 
	public void cadastrar(@RequestBody @Valid AutorFormDto dto){

	Autor autor = modelMapper.map(dto, Autor.class);
	
	
	 autores.add(autor);
	}
	
}
