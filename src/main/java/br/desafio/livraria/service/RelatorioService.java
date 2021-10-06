package br.desafio.livraria.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.desafio.livraria.dto.response.LivrosPorAutorDto;
import br.desafio.livraria.repository.LivroRepository;

@Service
public class RelatorioService {
	
	 @Autowired
	 private LivroRepository repository;
	 
	 
	  public List<LivrosPorAutorDto> relatorioLivrosPorAutor() {

	        return repository.relatorioLivrosPorAutor();
	    }

}
