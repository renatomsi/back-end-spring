package com.renatom.minhasfinancas.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.renatom.minhasfinancas.dto.UsuarioDTO;
import com.renatom.minhasfinancas.exception.ErroAutenticacao;
import com.renatom.minhasfinancas.exception.RegraNegocioException;
import com.renatom.minhasfinancas.model.entity.Usuario;
import com.renatom.minhasfinancas.service.LancamentoService;
import com.renatom.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private UsuarioService service;
	private LancamentoService lancamentoService;
	
//	Cria um construtor da classe service 
	public UsuarioController( UsuarioService service, LancamentoService lancamentoService) {
		this.service = service;
		this.lancamentoService = lancamentoService;
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
	
//	Criado um repositor para capturar a soma das despesas e receitas e no service fazer a logica de subtração para mostrar o saldo.
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldoUSuario(@PathVariable("id") Long id) {
		Optional<Usuario> usuario = service.obterPorId(id);
		
		if(!usuario.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		
		return ResponseEntity.ok(saldo);
	}

}
