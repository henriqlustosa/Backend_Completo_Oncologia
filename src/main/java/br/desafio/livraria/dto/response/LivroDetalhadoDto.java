package br.desafio.livraria.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LivroDetalhadoDto extends LivroDto {
	

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;
    private AutorDto autor;

}
