package com.renatom.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.renatom.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
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
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		// Cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//Ação / Execução 
		boolean result = repository.existsByEmail("usuario@email.com");
		
		// Verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		// cenário
		
		// ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		// verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void deveSalvarUmUsuarioNoBancoDeDados() {
		// cenario 
		Usuario usuario = criarUsuario();
		
		//acao
		Usuario usuarioSalvo = repository.save(usuario);
		
		//verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	
	@Test
	public void deveBuscarUmUsuarioPorEmailNaBaseERetornarOMesmo() {
		// cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		// acao
		Optional<Usuario> response =  repository.findByEmail("usuario@email.com");
		
		// verificação
		Assertions.assertThat(response.isPresent()).isTrue();
		
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmailNaBaseENaoRetornarNada() {
		// cenario

		
		// acao
		Optional<Usuario> response =  repository.findByEmail("usuario@email.com");
		
		// verificação
		Assertions.assertThat(response.isPresent()).isFalse();
		
	}
	
	
//	Criado uma classe static para criação de um usuario , para que nao fique sempre criando na mao.
	public static Usuario criarUsuario() {
		return Usuario.builder().name("usuario").email("usuario@email.com").senha("senha").build();
	}

}
