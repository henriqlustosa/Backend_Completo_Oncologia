package br.desafio.livraria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.desafio.livraria.modelo.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {
}
