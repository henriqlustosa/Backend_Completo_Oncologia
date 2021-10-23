	package br.desafio.livraria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.desafio.livraria.dto.response.LivrosPorAutor;
import br.desafio.livraria.dto.response.LivrosPorAutorDto;
import br.desafio.livraria.modelo.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {


	  @Query("select new br.desafio.livraria.dto.response.LivrosPorAutorDto(l.autor.nome, count(*), " +
	            "count(*) * 1.0 / (select count(*) from Livro l2) * 1.0 as percentual) " +
	            "from Livro l group by l.autor.nome order by percentual desc")
	    List<LivrosPorAutorDto> relatorioLivrosPorAutorDto();
	  
	    @Query("select l.autor.nome as autor, count(*) as quantidade, "
	            + "count(*) * 1.0 / (select count(*) from Livro) * 100.0 as percentual "
	            + "from Livro l group by l.autor order by percentual desc")
	    List<LivrosPorAutor> relatorioLivrosPorAutor();
	
}