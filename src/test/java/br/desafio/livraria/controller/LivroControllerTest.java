package br.desafio.livraria.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.desafio.livraria.dto.request.LivroFormDto;
import br.desafio.livraria.dto.request.LivroUpdateFormDto;

import br.desafio.livraria.dto.response.LivroDto;
import br.desafio.livraria.mocks.AutorFactory;
import br.desafio.livraria.mocks.LivroFactory;
import br.desafio.livraria.mocks.UsuarioFactory;
import br.desafio.livraria.modelo.Autor;
import br.desafio.livraria.modelo.Livro;
import br.desafio.livraria.modelo.Perfil;
import br.desafio.livraria.modelo.Usuario;
import br.desafio.livraria.repository.AutorRepository;
import br.desafio.livraria.repository.LivroRepository;
import br.desafio.livraria.repository.UsuarioRepository;

import br.desafio.livraria.infra.security.TokenService;




import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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

    
    @Autowired
    private LivroRepository livroRepository;

    private Long existingId ;
    private Long nonExistingId = 10L;
    private LivroDto livroResponseDto;
   
    private LivroDto livroAtualizaResponseDto;
    private LivroFormDto livroFormDto;
    private LivroUpdateFormDto livroUpdateFormDto;
    
    private Usuario usuarioLogado ;
    private Livro livro;
    private Livro livroUpdated;
	
    @Autowired
	private UsuarioRepository usuarioRepository;
    @Autowired
   	private AutorRepository autorRepository;
    
   

	@Autowired
	private TokenService tokenService;

    private String token;
    
    private Usuario usuario;
    private Autor autor;
    private static ModelMapper modelMapper = new ModelMapper();


    @BeforeEach
    void setUp() {
    	usuario = new Usuario(null, "Admin", "admin@mail.com", "SuperSecret123");
        usuario.adicionarPerfil(new Perfil(1l,"ROLE_ADMIN"));
    	usuarioLogado= usuarioRepository.save(usuario);
    	
		Authentication authentication = new UsernamePasswordAuthenticationToken(usuarioLogado, usuarioLogado.getLogin());
		token = "Bearer " + tokenService.gerarToken(authentication);
		
		autor = AutorFactory.criarAutorSemId();
		Autor autorSaved = autorRepository.save(autor);
		
		
		
		livro = LivroFactory.criarLivro("Lorem Ipsum", LocalDate.parse("2020-12-20"), 100, autorSaved,usuarioLogado);
		
		
		
		Livro livroSaved =livroRepository.save(livro);
		
		existingId = livroSaved.getId();
		
		livroUpdated = LivroFactory.criarLivro( existingId,"Updated Lorem Ipsum", LocalDate.parse("2020-12-20"), 100, autorSaved,usuarioLogado);
        livroResponseDto = modelMapper.map(livroSaved, LivroDto.class);
    
    	 
    
         
         livroFormDto = modelMapper.map(livroSaved, LivroFormDto.class);
         livroUpdateFormDto = modelMapper.map(livroUpdated, LivroUpdateFormDto.class);
         livroAtualizaResponseDto = modelMapper.map(livroUpdated, LivroDto.class);

    }

    @Test
    void findByIdShouldReturnAnBookWhenIdExists() throws Exception {
        mockMvc.perform(get("/livros/{id}", existingId).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(livroResponseDto.getId()));
    }
    @Test
    void criarShouldReturnBadRequestWhenInvalidDataWasProvided() throws Exception {
        String invalidData = "{}";

        mockMvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON).content(invalidData).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criarShouldReturnAnBookWhenSuccessfully() throws Exception {
        String validData = objectMapper.writeValueAsString(livroFormDto);

        mockMvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON).content(validData).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(header().exists("Location")).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(livroResponseDto.getId() + 1l));
        
    }

    @Test
    void atualizarShouldReturnAnBookWhenSuccessfully() throws Exception {
        String validData = objectMapper.writeValueAsString(livroUpdateFormDto);

        mockMvc.perform(put("/livros").contentType(MediaType.APPLICATION_JSON).content(validData).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(livroAtualizaResponseDto.getId()))
                .andExpect(jsonPath("$.titulo").value(livroAtualizaResponseDto.getTitulo()));
    }

    @Test
    void atualizarShouldReturnBadRequestWhenInvalidData() throws Exception {
        livroUpdateFormDto.setNumeroPaginas(10);
        String invalidData = objectMapper.writeValueAsString(livroUpdateFormDto);

        mockMvc.perform(put("/livros").contentType(MediaType.APPLICATION_JSON).content(invalidData).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletarShouldReturnBadRequestWhenInvalidId() throws Exception {
        mockMvc.perform(delete("/livros/{id}", nonExistingId).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarShouldDoNothingWhenValidId() throws Exception {
        mockMvc.perform(delete("/livros/{id}", existingId).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNoContent());
    }
   
}
