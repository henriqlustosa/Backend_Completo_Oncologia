package br.desafio.livraria.service;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.desafio.livraria.dto.request.UsuarioFormDto;
import br.desafio.livraria.dto.request.UsuarioUpdateFormDto;
import br.desafio.livraria.dto.response.UsuarioDto;
import br.desafio.livraria.exception.DomainException;
import br.desafio.livraria.exception.ResourceNotFoundException;
import br.desafio.livraria.infra.EnviadorDeEmail;
import br.desafio.livraria.modelo.Usuario;
import br.desafio.livraria.repository.PerfilRepository;
import br.desafio.livraria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PerfilRepository perfilRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private ModelMapper modelMapper;

    @Autowired
    private EnviadorDeEmail enviadorDeEmail;

	public Page<UsuarioDto> getUsuarios(Pageable paginacao) {
		Page<Usuario> usuarios = usuarioRepository.findAll(paginacao);
		return usuarios
				.map(t -> modelMapper.map(t, UsuarioDto.class));
	
		

	}
	@Transactional
	public UsuarioDto createUsuario(UsuarioFormDto usuarioFormDto) {
		Usuario usuarioToSave = modelMapper.map(usuarioFormDto, Usuario.class);
		
		usuarioToSave.setId(null);
		usuarioToSave.getPerfis().add(perfilRepository.getById(usuarioFormDto.getPerfilId()));

		String senha = gerarSenha();
		usuarioToSave.setSenha(bCryptPasswordEncoder.encode(senha));
		Usuario savedUsuario = usuarioRepository.save(usuarioToSave);
		
		String destinatario = savedUsuario.getEmail();
        String assunto = "Carteira - Boas vindas";
        String mensagem = String.format("Olá %s!\n\n Aqui estão seus dados de acesso ao sistema Carteira:" +
                "\nLogin:%s\nSenha:%s",
                savedUsuario.getNome(), savedUsuario.getLogin(), senha);

        enviadorDeEmail.enviarEmail(destinatario, assunto, mensagem);
		
		
		
		return modelMapper.map(savedUsuario,  UsuarioDto.class);
		
		
	}
	
	 @Transactional(readOnly = true)
	    public UsuarioDto mostrar(Long id) {
	        Usuario usuario = usuarioRepository.findById(id)
	        		.orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado: " + id));

	        return modelMapper.map(usuario, UsuarioDto.class);
	    }
	 
	 @Transactional(readOnly = true)
	    public UsuarioDto detalhar(Long id) {
	        try {
	            Usuario usuario = usuarioRepository.getById(id);

	            return modelMapper.map(usuario, UsuarioDto.class);
	        } catch (EntityNotFoundException e) {
	            throw new ResourceNotFoundException("Usuário inexistente");
	        }
	    }
	 
	    @Transactional
	    public UsuarioDto atualizar(UsuarioUpdateFormDto usuarioUpdateFormDto) {
	        try {
	            Usuario usuario = usuarioRepository.getById(usuarioUpdateFormDto.getId());
	            if (!usuarioUpdateFormDto.getLogin().equalsIgnoreCase(usuario.getLogin())) {
	                verificarLoginEmUso(usuarioUpdateFormDto.getLogin());
	            }
	            usuario.atualizarInformacoes(usuarioUpdateFormDto.getNome(),usuarioUpdateFormDto.getLogin());
	            usuarioRepository.save(usuario);

	            return modelMapper.map(usuario, UsuarioDto.class);
	        } catch (EntityNotFoundException e) {
	            throw new ResourceNotFoundException("Usuario inexistente: " + usuarioUpdateFormDto.getId());
	        }
	    }
	    
	    @Transactional
	    public void remover(Long id) {
	        try {
	        	usuarioRepository.deleteById(id);
	        } catch (EmptyResultDataAccessException e) {
	            throw new ResourceNotFoundException("Usuario inexistente: " + id);
	        } catch (DataIntegrityViolationException e) {
            throw new DomainException("Usuário não pode ser deletado");
        	}
	    }
	    
	    private void verificarLoginEmUso(String login) {
	        Optional<Usuario> usuario = usuarioRepository.findByLogin(login);

	        if (Objects.nonNull(usuario)) {
	            throw new DomainException("Login já em uso por outro usuário");
	        }
	    }
	    
	    private String gerarSenha() {
	        return new Random().nextInt(99999) + "";
	    }
}