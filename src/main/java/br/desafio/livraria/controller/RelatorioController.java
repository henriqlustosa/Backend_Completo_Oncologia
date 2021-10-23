package br.desafio.livraria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.livraria.dto.response.LivrosPorAutor;
import br.desafio.livraria.dto.response.LivrosPorAutorDto;
import br.desafio.livraria.service.RelatorioService;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {
	@Autowired
	private RelatorioService service;
	
	
	@GetMapping("/livraria/dto")
	public List<LivrosPorAutorDto> relatorioCarteiraDeInvestimentosDto(){
		return service.relatorioLivrosPorAutorDto();
	}

	@GetMapping("/livraria/")
	public List<LivrosPorAutor> relatorioCarteiraDeInvestimentos(){
		return service.relatorioLivrosPorAutor();
	}

}
