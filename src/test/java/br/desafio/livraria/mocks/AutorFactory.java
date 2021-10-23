package br.desafio.livraria.mocks;

import org.modelmapper.ModelMapper;

import br.desafio.livraria.dto.request.AutorFormDto;
import br.desafio.livraria.dto.request.AutorUpdateFormDto;
import br.desafio.livraria.modelo.Autor;
import lombok.var;

import java.time.LocalDate;
public class AutorFactory {
	private static ModelMapper modelMapper = new ModelMapper();
	
	

		public static Autor criarAutor() {
	        return new Autor(1L, "Henrique Lustosa", "henriqlustosa@gmail", LocalDate.now(),"Descricao do meu Minicurriculo");
	    }
		  public static Autor criarAutor(String nome, String email, LocalDate dataNascimento, String miniCurriculo) {
		        return new Autor(null, nome, email, dataNascimento, miniCurriculo);
		    }
	    public static Autor criarAutorSemId() {
	        return new Autor(null, "Henrique Lustosa", "henriqlustosa@gmail", LocalDate.now(),"Descricao do meu Minicurriculo");
	    }

	    public static AutorFormDto criarAutorFormDto() {
	        return modelMapper.map(criarAutor(), AutorFormDto.class);
	    }

	    public static AutorUpdateFormDto criarAutorUpdateFormComMesmoEmailDto() {
	        var usuario = new Autor(1L, " Update Henrique Lustosa", "henriqlustosa@gmail",LocalDate.now(),"Descricao do meu Minicurriculo");

	        return modelMapper.map(usuario, AutorUpdateFormDto.class);
	    }

	    public static AutorUpdateFormDto criarAutorUpdateFormComEmailDiferenteDto() {
	        var usuario = new Autor(1L, " Update Henrique Lustosa", "update@gmail",LocalDate.now(),"Descricao do meu Minicurriculo");

	        return modelMapper.map(usuario, AutorUpdateFormDto.class);
	    }

   
}
