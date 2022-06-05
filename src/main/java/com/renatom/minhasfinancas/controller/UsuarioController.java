package com.renatom.minhasfinancas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.renatom.minhasfinancas.dto.UsuarioDTO;
import com.renatom.minhasfinancas.exception.ErroAutenticacao;
import com.renatom.minhasfinancas.exception.RegraNegocioException;
import com.renatom.minhasfinancas.model.entity.Usuario;
import com.renatom.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private UsuarioService service;
	
//	Cria um construtor da classe service 
	public UsuarioController( UsuarioService service) {
		this.service = service;
	}
	
	@PostMapping("/auth")
	public ResponseEntity autenticarUser(@RequestBody UsuarioDTO dto) {
		try {
			Usuario usuarioAuth = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAuth);
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		
		Usuario usuario = Usuario.builder()
				.email(dto.getEmail())
				.name(dto.getName())
				.senha(dto.getSenha()).build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
			
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
