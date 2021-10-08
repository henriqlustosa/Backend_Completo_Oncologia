package br.desafio.livraria.service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.desafio.livraria.dto.request.AutorFormDto;
import br.desafio.livraria.dto.response.AutorDto;
import br.desafio.livraria.dto.response.MessageResponseDto;
import br.desafio.livraria.exception.AutorNotFoundException;
import br.desafio.livraria.modelo.Autor;
import br.desafio.livraria.repository.AutorRepository;



@Service

public class AutorService {
	@Autowired
	private AutorRepository autorRepository;

	ModelMapper modelMapper = new ModelMapper();

	public Page<AutorDto> listAll(Pageable paginacao) {
		Page<Autor> allAutores = autorRepository.findAll(paginacao);
		return allAutores
				.map(t -> modelMapper.map(t, AutorDto.class));
			
	}
	
	
	
	
	
	public AutorDto createAutor( AutorFormDto autorFormDto) {
    	
    	Autor autorToSave = modelMapper.map(autorFormDto, Autor.class);

        Autor savedAutor = autorRepository.save(autorToSave);
        
        return modelMapper.map(savedAutor, AutorDto.class);
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
	    
	    
	    
	    public AutorDto updateById(Long id, AutorFormDto pacienteDto) throws AutorNotFoundException {
	        verifyIfExists(id);

	        Autor autorToUpdate = modelMapper.map(pacienteDto, Autor.class);

	        Autor updatedAutor = autorRepository.save(autorToUpdate);
	        return modelMapper.map(updatedAutor, AutorDto.class);
	    }
	 
}
