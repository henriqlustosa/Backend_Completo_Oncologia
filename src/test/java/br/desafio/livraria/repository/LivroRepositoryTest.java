package br.desafio.livraria.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import br.desafio.livraria.dto.response.LivrosPorAutorDto;
import br.desafio.livraria.mocks.AutorFactory;
import br.desafio.livraria.mocks.LivroFactory;
import br.desafio.livraria.mocks.UsuarioFactory;
import br.desafio.livraria.modelo.Autor;
import br.desafio.livraria.modelo.Livro;
import br.desafio.livraria.modelo.Usuario;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LivroRepositoryTest {

	@Autowired
    private LivroRepository livroRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        Autor a1 = AutorFactory.criarAutor("André da Silva", "andre@mail.com", LocalDate.parse("1952-12-05"),
                "Curriculo");
        testEntityManager.persist(a1);
        Autor a2 = AutorFactory.criarAutor("Juliana Carvalho", "juliana@mail.com", LocalDate.parse("1952-12-05"),
                "Curriculo");
        testEntityManager.persist(a2);
        Autor a3 = AutorFactory.criarAutor("Fernanda Nogueira", "fernada@mail.com", LocalDate.parse("1952-12-05"),
                "Curriculo");
        testEntityManager.persist(a3);
        Usuario u1 = UsuarioFactory.criarUsuarioSemId();
        testEntityManager.persist(u1);

        Livro l1 = LivroFactory.criarLivro("Aprenda Java em 21 dias", LocalDate.parse("2004-03-12"), 100, a1,u1);
        testEntityManager.persist(l1);
        Livro l2 = LivroFactory.criarLivro("Aprenda a falar em público", LocalDate.parse("2004-03-12"), 100, a2,u1);
        testEntityManager.persist(l2);
        Livro l3 = LivroFactory.criarLivro("Otimizando seu tempo", LocalDate.parse("2004-03-12"), 100, a3,u1);
        testEntityManager.persist(l3);
        Livro l4 = LivroFactory.criarLivro("Como ser mais produtivo", LocalDate.parse("2004-04-21"), 100, a3,u1);
        testEntityManager.persist(l4);
    }

    
    @Test
    void shouldReturnRelatorioLivrosPorAutorDto() {
    	 List<LivrosPorAutorDto> relatorio = livroRepository.relatorioLivrosPorAutorDto();

        Assertions.assertThat(relatorio).hasSize(3)
                .extracting(LivrosPorAutorDto::getAutor,LivrosPorAutorDto::getQuantidade,LivrosPorAutorDto::getPercentual)
                .containsExactlyInAnyOrder(Assertions.tuple("Fernanda Nogueira", 2L, new BigDecimal("50.00")),
                        Assertions.tuple("André da Silva", 1L, new BigDecimal("25.00")),
                        Assertions.tuple("Juliana Carvalho", 1L, new BigDecimal("25.00")));
    }

	
}
