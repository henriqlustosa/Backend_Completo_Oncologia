package br.desafio.livraria.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.desafio.livraria.mocks.AutorFactory;
import br.desafio.livraria.modelo.Autor;
import lombok.var;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")

public class AutorRepositoryTest {
	 @Autowired
	    private AutorRepository autorRepository;

	    @Autowired
	    private TestEntityManager testEntityManager;

	    private Autor autor;
	
	 @BeforeEach
	    private void setUp() {
	        autor = AutorFactory.criarAutorSemId();

	        testEntityManager.persist(autor);
	    }

	    @Test
	    void findByEmailDeveriaRetornarUmAutorValido() {
	        var autorEncontrado = autorRepository.findByEmail(autor.getEmail());

	        assertEquals(autor.getId(), autorEncontrado.getId());
	        assertEquals(autor.getEmail(), autorEncontrado.getEmail());
	        assertEquals(autor.getNome(), autorEncontrado.getNome());
	    }

	    @Test
	    void findByNaoDeveriaTerRetornoComEmailNaoCadastrado() {
	        var autorEncontrado = autorRepository.findByEmail("any@mail.com");

	        assertNull(autorEncontrado);
	    }
	

}
