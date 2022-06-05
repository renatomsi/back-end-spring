package com.renatom.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renatom.minhasfinancas.exception.ErroAutenticacao;
import com.renatom.minhasfinancas.exception.RegraNegocioException;
import com.renatom.minhasfinancas.model.entity.Usuario;
import com.renatom.minhasfinancas.model.repository.UsuarioRepository;
import com.renatom.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;	

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuario não encontrado para o email informado!");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida!");
		}
		
		return usuario.get();
	}

	@Override
	// Anotação @Transactional abre uma transação com o banco e depois do save ele commita.
	@Transactional 
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
//			Verificamos se o email existe, se existir lança uma exception, 
//			Criada Classe RegraNegocioException no pacote de exception
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse email.");
		}
		
	}

}
