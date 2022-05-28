package com.renatom.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.renatom.minhasfinancas.model.entity.Usuario;
import com.renatom.minhasfinancas.model.repository.UsuarioRepository;
import com.renatom.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
//	Como no outro teste montamos os cenarios e as ações, porem mudou a forma de testar as exceptions, Professor ira gravar outra aula.
	
//  Para criar copias do repository usamos o Mock, que sao classes fakes para a realização de testes.
//	Criado classe setUp para setar o repository como uma classe fake e passando para o service o repository fake.
//	Chamando a anotação @MockBean ela transforma o repository em uma classe fake

	
	UsuarioService service;
	
	@MockBean
	UsuarioRepository repository;
	
	@Before
	public void setUp() {
		service = new UsuarioServiceImpl(repository);
	}
	
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		// cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		// acao
		Usuario result = service.autenticar(email, senha);
		
		// verificação 
		Assertions.assertThat(result).isNotNull();
	}
	
	
	@Test
	public void deveValidarEmail() {
		//cenario
//		Esta informando se tiver qualquer string no metodo existsByEmail deve retornar false para que consiga salvar o email.
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validarEmail("email@email.com");
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenario 
//		Esta informando se tiver qualquer string no metodo existsByEmail deve retornar True para que não consiga salvar o email.
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//acao
		service.validarEmail("email@email.com");
	}

}
