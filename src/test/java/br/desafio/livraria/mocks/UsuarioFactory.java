package br.desafio.livraria.mocks;

import org.modelmapper.ModelMapper;

import br.desafio.livraria.dto.request.UsuarioFormDto;
import br.desafio.livraria.dto.request.UsuarioUpdateFormDto;
import br.desafio.livraria.dto.response.UsuarioDto;
import br.desafio.livraria.modelo.Perfil;
import br.desafio.livraria.modelo.Usuario;

public class UsuarioFactory {
	 private static ModelMapper modelMapper = new ModelMapper();
	    
	   
		public static Usuario criarUsuario() {
			Usuario usuario = new Usuario(1L, "Henrique Lustosa", "henriqlustosa", "123123");
			usuario.adicionarPerfil(new Perfil(1l,"ROLE_ADMIN"));
			return usuario;
	    }

		
	    public static Usuario criarUsuarioSemId() {
	    	Usuario usuario = new Usuario(null, "Henrique Lustosa", "henriqlustosa", "123123");
	    	usuario.adicionarPerfil(new Perfil(1l,"ROLE_ADMIN"));
			return usuario;
	    }

	    public static UsuarioFormDto criarUsuarioFormDto() {
	    	UsuarioFormDto usuarioFormDto = modelMapper.map(criarUsuario(), UsuarioFormDto.class);
	    	usuarioFormDto.setPerfilId(1L);

	        return usuarioFormDto;
	    }
	    public static UsuarioDto criarUsuarioResponseDto() {
	        return modelMapper.map(criarUsuario(), UsuarioDto.class);
	    }
	    public static UsuarioUpdateFormDto criarUsuarioUpdateFormComMesmoLoginDto() {
	        Usuario usuario = new Usuario(1L, "Updated Henrique Lustosa", "henriqlustosa", "123123");

	        return modelMapper.map(usuario, UsuarioUpdateFormDto.class);
	    }

	    public static UsuarioUpdateFormDto criarUsuarioUpdateFormComLoginDiferenteDto() {
	        Usuario usuario = new Usuario(1L, "Updated Henrique Lustosa", "updatedmail", "123123");

	        return modelMapper.map(usuario, UsuarioUpdateFormDto.class);
	    }
	    public static UsuarioDto criarUsuarioAtualizadoComMesmoLoginResponseDto() {
	        return modelMapper.map(criarUsuarioUpdateFormComMesmoLoginDto(), UsuarioDto.class);
	    }

	    public static UsuarioDto criarUsuarioAtualizadoComLoginDiferenteResponseDto() {
	        return modelMapper.map(criarUsuarioUpdateFormComLoginDiferenteDto(), UsuarioDto.class);
	    }

}
