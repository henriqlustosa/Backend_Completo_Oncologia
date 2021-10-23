package br.desafio.livraria.dto.response;

import java.math.BigDecimal;
import java.math.RoundingMode;


import lombok.Getter;

@Getter

public class LivrosPorAutorDto {
	private String autor;
    private Long quantidade;
    private BigDecimal percentual;

    public LivrosPorAutorDto(String autor, Long quantidade, Double percentual) {
        this.autor = autor;
        this.quantidade = quantidade;
        setPercentual(percentual);
    }

    private void setPercentual(Double percentual) {
        this.percentual = BigDecimal.valueOf(percentual * 100).setScale(2, RoundingMode.HALF_UP);
    }
}