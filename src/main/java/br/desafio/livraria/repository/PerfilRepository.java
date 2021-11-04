package br.desafio.livraria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.desafio.livraria.modelo.Perfil;


@Repository
public interface PerfilRepository extends JpaRepository<Perfil,Long> {

}