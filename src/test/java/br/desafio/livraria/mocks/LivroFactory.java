package br.desafio.livraria.mocks;

import org.modelmapper.ModelMapper;

import br.desafio.livraria.dto.request.LivroFormDto;
import br.desafio.livraria.dto.request.LivroUpdateFormDto;
import br.desafio.livraria.dto.response.LivroDetalhadoDto;
import br.desafio.livraria.dto.response.LivroDto;
import br.desafio.livraria.modelo.Autor;
import br.desafio.livraria.modelo.Livro;
import br.desafio.livraria.modelo.Usuario;

import java.time.LocalDate;
public class LivroFactory {
	

    private static ModelMapper modelMapper = new ModelMapper();

    public static Livro criarLivro() {
        return new Livro(1L, "Lorem Ipsum", LocalDate.parse("2020-12-20"), 100, AutorFactory.criarAutor(),UsuarioFactory.criarUsuario());
    }
    public static Livro criarLivroSemId() {
        return new Livro(null, "Lorem Ipsum", LocalDate.parse("2020-12-20"), 100, AutorFactory.criarAutor(),UsuarioFactory.criarUsuario());
    }

    public static Livro criarLivro(String titulo, LocalDate dataLancamento, Integer numeroPaginas, Autor autor, Usuario usuario) {
        return new Livro(null, titulo, dataLancamento, numeroPaginas, autor, usuario);
    }
    public static Livro criarLivro( Long id,String titulo, LocalDate dataLancamento, Integer numeroPaginas, Autor autor, Usuario usuario) {
        return new Livro(id, titulo, dataLancamento, numeroPaginas, autor, usuario);
    }
    public static Livro criarLivroAtualizado() {
        return new Livro(1L, "Updated Lorem Ipsum", LocalDate.parse("2021-10-20"), 120, AutorFactory.criarAutor(),UsuarioFactory.criarUsuario());
    }

    public static LivroDto criarLivroResponseDto() {
        return modelMapper.map(criarLivro(), LivroDto.class);
    }
    public static LivroDetalhadoDto criarLivroDetalhadoDto() {
        return modelMapper.map(criarLivro(), LivroDetalhadoDto.class);
    }
    public static LivroDto criarLivroAtualizadoResponseDto() {
        return modelMapper.map(criarLivroAtualizado(), LivroDto.class);
    }

    public static LivroFormDto criarLivroFormDto() {
        return modelMapper.map(criarLivro(), LivroFormDto.class);
    }

    public static LivroUpdateFormDto criarLivroUpdateFormDto() {
        return modelMapper.map(criarLivroAtualizado(), LivroUpdateFormDto.class);
    }
    
	
	
    public static LivroUpdateFormDto criarLivroUpdateFormDtoComIdInvalido() {
        return LivroUpdateFormDto.builder().id(200L).build();
    }
}
