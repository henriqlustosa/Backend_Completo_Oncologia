package br.desafio.livraria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.desafio.livraria.dto.request.LivroDto;
import br.desafio.livraria.dto.response.MessageResponseDto;
import br.desafio.livraria.exception.AutorNotFoundException;
import br.desafio.livraria.exception.LivroNotFoundException;
import br.desafio.livraria.modelo.Livro;
import br.desafio.livraria.repository.LivroRepository;


@Service
public class LivroService {

	@Autowired
	private LivroRepository livroRepository;

	ModelMapper modelMapper = new ModelMapper();

	public List<LivroDto> listAll() {
		List<Livro> allLivros = livroRepository.findAll();
		return allLivros
				.stream()
				.map(t -> modelMapper.map(t, LivroDto.class))
				.collect(Collectors.toList());
	}
	public MessageResponseDto createLivro( LivroDto livroDto) {
    	
    	Livro personToSave = modelMapper.map(livroDto, Livro.class);

        Livro savedLivro = livroRepository.save(personToSave);
        return createMessageResponse(savedLivro.getId(), "Created person with ID ");
    }
	
	 private MessageResponseDto createMessageResponse(Long id, String message) {
	        return MessageResponseDto
	                .builder()
	                .message(message + id)
	                .build();
	    }
	 
	   public LivroDto findById(Long id) throws LivroNotFoundException {
	        Livro Livro = verifyIfExists(id);

	        return modelMapper.map(Livro, LivroDto.class);
	    }
		  public void delete(Long id) throws LivroNotFoundException {
		        verifyIfExists(id);
		        livroRepository.deleteById(id);
		    }
	    private Livro verifyIfExists(Long id) throws LivroNotFoundException {
	        return livroRepository.findById(id)
	                .orElseThrow(() -> new LivroNotFoundException(id));
	    }
	    
	    public MessageResponseDto updateById(Long id, LivroDto livroDto) throws LivroNotFoundException {
	        verifyIfExists(id);

	        Livro LivroToUpdate = modelMapper.map(livroDto, Livro.class);

	        Livro updatedPerson = livroRepository.save(LivroToUpdate);
	        return createMessageResponse(updatedPerson.getId(), "Updated person with ID ");
	    }
	 
}