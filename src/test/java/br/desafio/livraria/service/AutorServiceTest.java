package br.desafio.livraria.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.desafio.livraria.modelo.*;
import br.desafio.livraria.dto.request.AutorUpdateFormDto;
import br.desafio.livraria.exception.DomainException;
import br.desafio.livraria.exception.ResourceNotFoundException;
import br.desafio.livraria.mocks.AutorFactory;
import br.desafio.livraria.repository.AutorRepository;


import static org.mockito.Mockito.when;

import java.util.Optional;

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
	private AutorUpdateFormDto autorUpdateFormComEmailDiferenteDto = AutorFactory
            .criarUsuarioUpdateFormComEmailDiferenteDto();
	private AutorUpdateFormDto autorUpdateFormComMesmoEmailDto = AutorFactory
            .criarAutorUpdateFormComMesmoEmailDto();
	
	
    @Test
    void findByIdDeveRetornarAutorDto() {
        long validId = 1L;
        when(autorRepository.findById(anyLong())).thenReturn(Optional.of(autor));
        var autorResponseDto = autorService.findById(validId);
        

        assertEquals(validId, autorResponseDto.getId());
        verify(autorRepository, times(1)).findById(validId);
    }
    
    
    @Test
    void findByIdDeveLancarResourceNotFoundExceptionQuandoIdInvalido() {
		doThrow(EntityNotFoundException.class).when(autorRepository).findById(anyLong());

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
}
