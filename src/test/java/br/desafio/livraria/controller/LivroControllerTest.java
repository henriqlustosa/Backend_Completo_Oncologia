package br.desafio.livraria.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.desafio.livraria.dto.request.LivroFormDto;
import br.desafio.livraria.dto.request.LivroUpdateFormDto;
import br.desafio.livraria.dto.response.LivroDetalhadoDto;
import br.desafio.livraria.dto.response.LivroDto;
import br.desafio.livraria.mocks.LivroFactory;
import br.desafio.livraria.service.LivroService;
import br.desafio.livraria.exception.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LivroControllerTest {
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LivroService livroService;

    private Long existingId = 1L;
    private Long nonExistingId = 10L;
    private LivroDto livroResponseDto;
    private LivroDetalhadoDto livroResponseDetalhadoDto;
    private LivroDto livroAtualizaResponseDto;
    private LivroFormDto livroFormDto;
    private LivroUpdateFormDto livroUpdateFormDto;
    

    @BeforeEach
    void setUp() {
        livroResponseDto = LivroFactory.criarLivroResponseDto();
    	livroResponseDetalhadoDto = LivroFactory.criarLivroDetalhadoDto();
    	
       
         
         livroFormDto = LivroFactory.criarLivroFormDto();
         livroUpdateFormDto = LivroFactory.criarLivroUpdateFormDto();
         livroAtualizaResponseDto = LivroFactory.criarLivroAtualizadoResponseDto();

         when(livroService.findById(existingId)).thenReturn(livroResponseDetalhadoDto);
         when(livroService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
         when(livroService.createLivro(any(LivroFormDto.class))).thenReturn(livroResponseDto);
         when(livroService.update(any(LivroUpdateFormDto.class))).thenReturn(livroAtualizaResponseDto);
         doThrow(ResourceNotFoundException.class).when(livroService).delete(nonExistingId);
         doNothing().when(livroService).delete(existingId);
    }

    @Test
    void findByIdShouldReturnAnBookWhenIdExists() throws Exception {
        mockMvc.perform(get("/livros/{id}", existingId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(livroResponseDto.getId()));
    }
    @Test
    void criarShouldReturnBadRequestWhenInvalidDataWasProvided() throws Exception {
        String invalidData = "{}";

        mockMvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criarShouldReturnAnBookWhenSuccessfully() throws Exception {
        String validData = objectMapper.writeValueAsString(livroFormDto);

        mockMvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(header().exists("Location")).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(livroResponseDto.getId()));
    }

    @Test
    void atualizarShouldReturnAnBookWhenSuccessfully() throws Exception {
        String validData = objectMapper.writeValueAsString(livroUpdateFormDto);

        mockMvc.perform(put("/livros").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(livroAtualizaResponseDto.getId()))
                .andExpect(jsonPath("$.titulo").value(livroAtualizaResponseDto.getTitulo()));
    }

    @Test
    void atualizarShouldReturnBadRequestWhenInvalidData() throws Exception {
        livroUpdateFormDto.setNumeroPaginas(10);
        String invalidData = objectMapper.writeValueAsString(livroUpdateFormDto);

        mockMvc.perform(put("/livros").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletarShouldReturnBadRequestWhenInvalidId() throws Exception {
        mockMvc.perform(delete("/livros/{id}", nonExistingId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarShouldDoNothingWhenValidId() throws Exception {
        mockMvc.perform(delete("/livros/{id}", existingId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
   
}
