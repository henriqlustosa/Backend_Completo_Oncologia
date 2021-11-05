package br.desafio.livraria.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import br.desafio.livraria.infra.security.TokenService;
import br.desafio.livraria.mocks.UsuarioFactory;
import br.desafio.livraria.modelo.Usuario;
import br.desafio.livraria.repository.UsuarioRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class RelatorioControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TokenService tokenService;
	
	private String token;
	    
	private Usuario usuario;
	private Long user_id;
	@BeforeEach
	void setUp() {
		usuario = UsuarioFactory.criarUsuarioSemId();
		Usuario usuarioSaved =usuarioRepository.save(usuario);
		user_id = usuarioSaved.getId();
		Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, usuario.getLogin());

		this.token = "Bearer " + tokenService.gerarToken(authentication);
	}
	
	@Test
	void deveriaRetornarRelatorioDeLivrosPorAutor() throws Exception {

		String jsonAutor = "{\"nome\":\"Henrique Lustosa\",\"email\":\"henrique@email.com\",\"dataNascimento\":\"1982-08-12\",\"miniCurriculo\":\"Teste de minicurriculo\"}";
		MvcResult resultado = mvc
		.perform(post("/autores")
		.contentType(MediaType.APPLICATION_JSON)
		.content(jsonAutor).header(HttpHeaders.AUTHORIZATION, token))
		.andReturn();
		
		Integer id = JsonPath.read(resultado.getResponse().getContentAsString(), "$.id");
				
		String jsonLivro = "{\"titulo\":\"Java em 21 dias\",\"dataDeLancamento\":\"2020-12-18\",\"numeroPaginas\":110,\"autor_id\":"+id+",\"usuario_id\":" + user_id +"}";
		
		mvc
		.perform(post("/livros")
		.contentType(MediaType.APPLICATION_JSON)
		.content(jsonLivro).header(HttpHeaders.AUTHORIZATION, token));

		String jsonRelatorio = "[{\"autor\":\"Henrique Lustosa\",\"quantidade\":1,\"percentual\":100.0}]";
		
		mvc
		.perform(get("/relatorios/livraria/dto")
		.contentType(MediaType.APPLICATION_JSON)
		.content(jsonLivro).header(HttpHeaders.AUTHORIZATION, token))
		.andExpect(status().isOk())
		.andExpect(content().json(jsonRelatorio));
		
		
	}
}
