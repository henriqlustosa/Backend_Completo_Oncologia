package br.desafio.livraria.modelo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "autores")
public class Autor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String nome;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private LocalDate dataNascimento;
	@Column(nullable = false)
	private String miniCurriculo;


	public void atualizarInformacoes(String nome, String email, LocalDate dataNascimento, String miniCurriculo) {
		this.nome = nome;
		this.email = email;
		this.dataNascimento = dataNascimento;
		this.miniCurriculo = miniCurriculo;

	}
}
