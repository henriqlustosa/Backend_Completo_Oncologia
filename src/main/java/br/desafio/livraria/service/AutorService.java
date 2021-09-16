package br.desafio.livraria.service;

import java.util.List;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import br.desafio.livraria.dto.request.AutorDto;
import br.desafio.livraria.dto.response.MessageResponseDto;
import br.desafio.livraria.modelo.Autor;
import br.desafio.livraria.repository.AutorRepository;



@Service

public class AutorService {
	@Autowired
	private AutorRepository autorRepository;

	ModelMapper modelMapper = new ModelMapper();

	public List<AutorDto> listAll() {
		List<Autor> allAutores = autorRepository.findAll();
		return allAutores
				.stream()
				.map(t -> modelMapper.map(t, AutorDto.class))
				.collect(Collectors.toList());
	}
	public MessageResponseDto createAutor( AutorDto pacienteDto) {
    	
    	Autor personToSave = modelMapper.map(pacienteDto, Autor.class);

        Autor savedAutor = autorRepository.save(personToSave);
        return createMessageResponse(savedAutor.getId(), "Created person with ID ");
    }
	
	 private MessageResponseDto createMessageResponse(Long id, String message) {
	        return MessageResponseDto
	                .builder()
	                .message(message + id)
	                .build();
	    }
}
