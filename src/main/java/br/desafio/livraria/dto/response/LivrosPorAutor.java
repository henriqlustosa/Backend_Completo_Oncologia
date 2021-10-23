package br.desafio.livraria.dto.response;

import java.math.BigDecimal;

public interface LivrosPorAutor {

    String getAutor();

    Long getQuantidade();

    BigDecimal getPercentual();
}
