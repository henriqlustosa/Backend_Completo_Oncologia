package br.desafio.livraria.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import br.desafio.livraria.modelo.*;
import br.desafio.livraria.dto.request.AutorFormDto;
import br.desafio.livraria.dto.request.AutorUpdateFormDto;
import br.desafio.livraria.exception.DomainException;
import br.desafio.livraria.exception.ResourceNotFoundException;
import br.desafio.livraria.mocks.AutorFactory;
import br.desafio.livraria.repository.AutorRepository;
import lombok.var;

import static org.mockito.Mockito.when;



import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {
	
	@Mock
	private AutorRepository autorRepository ;
	
	@InjectMocks
	private AutorService autorService;
	
	private Autor autor = AutorFactory.criarAutor();
	
	private AutorFormDto autorFormDto = AutorFactory.criarAutorFormDto();
	private AutorUpdateFormDto autorUpdateFormComEmailDiferenteDto = AutorFactory
            .criarAutorUpdateFormComEmailDiferenteDto();
	private AutorUpdateFormDto autorUpdateFormComMesmoEmailDto = AutorFactory
            .criarAutorUpdateFormComMesmoEmailDto();
	
	
    @Test
    void findByIdDeveRetornarAutorDto() {
        long validId = 1L;
        when(autorRepository.getById(anyLong())).thenReturn(autor);
        var autorResponseDto = autorService.findById(validId);
        

        assertEquals(validId, autorResponseDto.getId());
        verify(autorRepository, times(1)).getById(validId);
    }
    
    
    @Test
    void findByIdDeveLancarResourceNotFoundExceptionQuandoIdInvalido() {
		doThrow(EntityNotFoundException.class).when(autorRepository).getById(anyLong());

        assertThrows(ResourceNotFoundException.class, () -> autorService.findById(1L));
    }
    
    @Test
    void atualizarDeveLancarResourceNotFoundExceptionQuandoIdInvalido() {
        doThrow(EntityNotFoundException.class).when(autorRepository).getById(anyLong());

        assertThrows(ResourceNotFoundException.class,
                () -> autorService.update(autorUpdateFormComEmailDiferenteDto));
    }
    @Test
    void atualizarDeveLancarDomainExceptionQuandoAutorQuiserUtilizarEmailRegistrado() {
        when(autorRepository.getById(anyLong())).thenReturn(autor);
        when(autorRepository.findByEmail(anyString())).thenReturn(autor);

        assertThrows(DomainException.class, () -> autorService.update(autorUpdateFormComEmailDiferenteDto));
        verify(autorRepository, times(0)).save(any());
    }
    
    @Test
    void atualizarDeveRetornarautorAtualizadoComEmailDiferente() {
        when(autorRepository.getById(anyLong())).thenReturn(autor);
        var autorAtualizado = autorService.update(autorUpdateFormComEmailDiferenteDto);

        assertEquals(autorAtualizado.getNome(), autorUpdateFormComEmailDiferenteDto.getNome());
        verify(autorRepository, times(1)).save(any());
    }

    @Test
    void atualizarDeveRetornarautorAtualizadoComMesmoEmail() {
        when(autorRepository.getById(anyLong())).thenReturn(autor);
        var autorAtualizado = autorService.update(autorUpdateFormComMesmoEmailDto);

        assertEquals(autorAtualizado.getNome(), autorUpdateFormComMesmoEmailDto.getNome());
        verify(autorRepository, times(1)).save(any());
    }
    
    
    
    
    @Test
    void deletarDeveLancarResourceNotFoundExceptionQuandoIdInvalido() {
        doThrow(EmptyResultDataAccessException.class).when(autorRepository).deleteById(1L);

        assertThrows(ResourceNotFoundException.class, () -> autorService.delete(1l));
        verify(autorRepository, times(1)).deleteById(anyLong());
    }
    
    @Test
    void criarDeveCriarautorQuandoEmailNaoEstiverEmRegistrado() {
    	
    	when(autorRepository.save(Mockito.any(Autor.class))).thenAnswer(i -> i.getArguments()[0]);
        var autorResponseDto = autorService.createAutor(autorFormDto);

        assertEquals(autorFormDto.getNome(), autorResponseDto.getNome());
       
        verify(autorRepository, times(1)).save(any());
    }
    
    
    @Test
    void deletarDeveLancarDomainExceptionQuandoautorNaoPodeSerExcluido() {
        doThrow(DataIntegrityViolationException.class).when(autorRepository).deleteById(1L);

        assertThrows(DomainException.class, () -> autorService.delete(1l));
        verify(autorRepository, times(1)).deleteById(anyLong());
    }
    
    
    @Test
    void deletarNaoDeveRetornarNadaQuandoIdExistir() {
        long validId = 1l;
        autorRepository.deleteById(validId);

        verify(autorRepository, times(1)).deleteById(validId);
    }

}
