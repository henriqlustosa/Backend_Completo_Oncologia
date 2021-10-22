package br.desafio.livraria.service;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.desafio.livraria.dto.request.AutorFormDto;
import br.desafio.livraria.dto.request.AutorUpdateFormDto;
import br.desafio.livraria.dto.response.AutorDto;
import br.desafio.livraria.exception.DomainException;
import br.desafio.livraria.exception.ResourceNotFoundException;
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
	
	
	

	 
	 
	  public void delete(Long id) {
	        verifyIfExists(id);
	        autorRepository.deleteById(id);
	    }
	  
	  
	 
	   public AutorDto findById(Long id)  {
		   
		   
	        Autor autor = verifyIfExists(id);

	        return modelMapper.map(autor, AutorDto.class);
	    }
	 
	   
	   
	    private Autor verifyIfExists(Long id)  {
	        return autorRepository.findById(id)
	        		.orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));
	    }
	    
	    
	    
	    public AutorDto update(AutorUpdateFormDto autorUpdateFormDto)  {
	  

	        try {
	            var autor = autorRepository.getById( autorUpdateFormDto.getId());
	            if (!autorUpdateFormDto.getEmail().equalsIgnoreCase(autor.getEmail())) {
	                verificarEmailEmUso(autorUpdateFormDto.getEmail());
	            }
	        
	            autor.atualizarInformacoes(autorUpdateFormDto.getNome() ,autorUpdateFormDto.getEmail(), autorUpdateFormDto.getDataNascimento(),autorUpdateFormDto.getMiniCurriculo());
	            autorRepository.save(autor);

	            return modelMapper.map(autor, AutorDto.class);
	        } catch (EntityNotFoundException e) {
	            throw new ResourceNotFoundException("Autor inexistente: " + autorUpdateFormDto.getId());
	        }
	    }
	    private void verificarEmailEmUso(String email) {
	        var autor = autorRepository.findByEmail(email);

	        if (Objects.nonNull(autor)) {
	            throw new DomainException("Login já em uso por outro usuário");
	        }
	    }
}
