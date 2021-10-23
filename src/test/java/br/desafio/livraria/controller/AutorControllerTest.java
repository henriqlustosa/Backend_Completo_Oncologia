package br.desafio.livraria.controller;

import javax.transaction.Transactional;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.desafio.livraria.mocks.AutorFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AutorControllerTest {
	
	 @Autowired
	    private MockMvc mvc;

	 @Autowired
	  private ObjectMapper objectMapper;
	 @Test
	    void naoDeveriaCadastrarAutorComDadosIncompletos() throws Exception {
	        String json = "{}";

	        mvc.perform(post("/autores").contentType(MediaType.APPLICATION_JSON).content(json))
	                .andExpect(status().isBadRequest());
	    }

	    @Test
	    void deveriaCadastrarUmAutorComDadosCompletos() throws Exception {
	        String json = objectMapper.writeValueAsString(AutorFactory.criarAutorFormDto());

	        mvc.perform(post("/autores").contentType(MediaType.APPLICATION_JSON).content(json))
	                .andExpect(status().isCreated()).andExpect(header().exists("Location")).andExpect(content().json(json))
	                .andExpect(jsonPath("$.id").exists());
	    }

}
