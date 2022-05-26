package com.renatom.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renatom.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
//	Usamos Optional pois pode retornar ou nao um usuario com o email que foi enviado pelo parametro.
//	Ja que passamos o metodo findByEmail não precisamos fazer uma query pois o spring data reconhece e faz a busca sozinho.
//	Nome dessa feature é query Methods.
	Optional<Usuario> findByEmail(String email);
	
//	Caso queiramos buscar por Email e Nome basta usar dessa forma:
//	Optional<Usuario> findByEmailAndNome(String email, String nome);
	
//	Podemos verficiar e retornar apenas um booleano para verificar se o email existe na base:
	boolean existsByEmail(String email);

}
