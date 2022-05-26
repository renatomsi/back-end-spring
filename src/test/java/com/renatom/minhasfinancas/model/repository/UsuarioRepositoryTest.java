package com.renatom.minhasfinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.renatom.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
//	Criamos uma pacote com o na pasta de test e criamos uma classe com o nome de UsuarioRepositoryTest
//	Criamos anotação @Test para informar que é um teste.
//	Criamos uma classe que retorna void cadastrando um usuario com email "usuario@email.com" e depois usamos o metodo que criamos.
//	 Por fim verificamos se o resultado é verdadeiro , se Sim o metodo esta funcionando.
//	Obs: Apos atualização do Spring, prescisamos mudar umas anotations Ex: 
//	@Test import org.junit.jupiter.api.Test;
//	@ExtendWith import org.junit.jupiter.api.extension.ExtendWith;
//	SpringExtension.class import org.springframework.test.context.junit.jupiter.SpringExtension;
//	Informando o profile de test usando o db em memoria
	
	 
	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		// Cenario
		Usuario usuario = Usuario.builder().name("usuario").email("usuario@email.com").build();
		repository.save(usuario);
		
		//Ação / Execução 
		boolean result = repository.existsByEmail("usuario@email.com");
		
		// Verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		// cenário
		repository.deleteAll();
		
		// ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		// verificação
		Assertions.assertThat(result).isFalse();
	}

}
