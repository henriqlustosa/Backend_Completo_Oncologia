package br.desafio.livraria.controller;

import javax.transaction.Transactional;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.desafio.livraria.infra.security.TokenService;
import br.desafio.livraria.mocks.AutorFactory;
import br.desafio.livraria.mocks.UsuarioFactory;
import br.desafio.livraria.modelo.Perfil;
import br.desafio.livraria.modelo.Usuario;
import br.desafio.livraria.repository.PerfilRepository;
import br.desafio.livraria.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
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

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

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

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TokenService tokenService;

	private String token;

	private Usuario usuario;

	@BeforeEach
	void setUp() {
		usuario = UsuarioFactory.criarUsuarioSemId();
		usuarioRepository.save(usuario);
		Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, usuario.getLogin());

		this.token = "Bearer " + tokenService.gerarToken(authentication);
	}

	@Test
	void naoDeveriaCadastrarAutorComDadosIncompletos() throws Exception {
		String json = "{}";

		mvc.perform(post("/autores").contentType(MediaType.APPLICATION_JSON).content(json)
				.header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isBadRequest());
	}

	@Test
	void deveriaCadastrarUmAutorComDadosCompletos() throws Exception {
		String json = objectMapper.writeValueAsString(AutorFactory.criarAutorFormDto());

       mvc.perform(post("/autores").header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(header().exists("Location"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists());

	}

}
