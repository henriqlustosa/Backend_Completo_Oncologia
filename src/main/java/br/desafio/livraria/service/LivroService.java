package br.desafio.livraria.service;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.desafio.livraria.dto.request.LivroFormDto;
import br.desafio.livraria.dto.response.LivroDto;
import br.desafio.livraria.dto.response.MessageResponseDto;

import br.desafio.livraria.exception.LivroNotFoundException;
import br.desafio.livraria.modelo.Livro;
import br.desafio.livraria.repository.LivroRepository;


@Service
public class LivroService {

	@Autowired
	private LivroRepository livroRepository;

	ModelMapper modelMapper = new ModelMapper();

	public Page<LivroDto> listAll(Pageable paginacao) {
		Page<Livro> allLivros = livroRepository.findAll(paginacao);
		return allLivros
				.map(t -> modelMapper.map(t, LivroDto.class));
			
	}
	
	public LivroDto createLivro( LivroFormDto livroDto) {
    	
    	Livro livroToSave = modelMapper.map(livroDto, Livro.class);

        Livro savedLivro = livroRepository.save(livroToSave);
        return modelMapper.map(savedLivro,LivroDto.class);
    }
	
	 private MessageResponseDto createMessageResponse(Long id, String message) {
	        return MessageResponseDto
	                .builder()
	                .message(message + id)
	                .build();
	    }
	 
	   public LivroFormDto findById(Long id) throws LivroNotFoundException {
	        Livro Livro = verifyIfExists(id);

	        return modelMapper.map(Livro, LivroFormDto.class);
	    }
		  public void delete(Long id) throws LivroNotFoundException {
		        verifyIfExists(id);
		        livroRepository.deleteById(id);
		    }
	    private Livro verifyIfExists(Long id) throws LivroNotFoundException {
	        return livroRepository.findById(id)
	                .orElseThrow(() -> new LivroNotFoundException(id));
	    }
	    
	    public MessageResponseDto updateById(Long id, LivroFormDto livroDto) throws LivroNotFoundException {
	        verifyIfExists(id);

	        Livro LivroToUpdate = modelMapper.map(livroDto, Livro.class);

	        Livro updatedLivro = livroRepository.save(LivroToUpdate);
	        return createMessageResponse(updatedLivro.getId(), "Atualizado um livro com ID");
	    }
	 
}