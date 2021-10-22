package br.desafio.livraria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.desafio.livraria.modelo.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {
	  Autor findByEmail(String email);
}
