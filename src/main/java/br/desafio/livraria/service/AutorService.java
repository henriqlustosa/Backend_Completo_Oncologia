package br.desafio.livraria.service;

import java.util.List;
import java.util.stream.Collectors;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import br.desafio.livraria.dto.request.AutorDto;
import br.desafio.livraria.dto.response.MessageResponseDto;
import br.desafio.livraria.exception.AutorNotFoundException;
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
        
        return createMessageResponse(savedAutor.getId(), "Criado um Autor com ID ");
    }
	
	
	
	 private MessageResponseDto createMessageResponse(Long id, String message) {
	        return MessageResponseDto
	                .builder()
	                .message(message + id)
	                .build();
	    }
	 
	 
	  public void delete(Long id) throws AutorNotFoundException {
	        verifyIfExists(id);
	        autorRepository.deleteById(id);
	    }
	  
	  
	 
	   public AutorDto findById(Long id) throws AutorNotFoundException {
	        Autor autor = verifyIfExists(id);

	        return modelMapper.map(autor, AutorDto.class);
	    }
	 
	   
	   
	    private Autor verifyIfExists(Long id) throws AutorNotFoundException {
	        return autorRepository.findById(id)
	                .orElseThrow(() -> new AutorNotFoundException(id));
	    }
	    
	    
	    
	    public MessageResponseDto updateById(Long id, AutorDto pacienteDto) throws AutorNotFoundException {
	        verifyIfExists(id);

	        Autor autorToUpdate = modelMapper.map(pacienteDto, Autor.class);

	        Autor updatedPerson = autorRepository.save(autorToUpdate);
	        return createMessageResponse(updatedPerson.getId(), "Atualizado um autor com ID ");
	    }
	 
}
