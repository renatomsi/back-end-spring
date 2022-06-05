package com.renatom.minhasfinancas.service;

import java.util.Optional;


import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.renatom.minhasfinancas.exception.ErroAutenticacao;
import com.renatom.minhasfinancas.exception.RegraNegocioException;
import com.renatom.minhasfinancas.model.entity.Usuario;
import com.renatom.minhasfinancas.model.repository.UsuarioRepository;
import com.renatom.minhasfinancas.service.impl.UsuarioServiceImpl;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
//	Como no outro teste montamos os cenarios e as ações, porem mudou a forma de testar as exceptions, Professor ira gravar outra aula.
	
//  Para criar copias do repository usamos o Mock, que sao classes fakes para a realização de testes.
//	Criado classe setUp para setar o repository como uma classe fake e passando para o service o repository fake.
//	Chamando a anotação @MockBean ela transforma o repository em uma classe fake

	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	@Test
	public void deveSalvarUsuario() {
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1l).name("nome").email("email@email.com").senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		// verificação 
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getName()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		// cenario
		String  email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		// Retorna uma regra de negocio exception ao tentar validar um email ja cadastrado.
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//acao
		service.salvarUsuario(usuario);
		
		// verificação
		// Verifica se o metodo repository.save nunca foi chamado.
		Mockito.verify(repository, Mockito.never()).save(usuario);
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
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
		Assertions.assertThat(exception)
			.isInstanceOf(ErroAutenticacao.class)
			.hasMessage("Usuario não encontrado para o email informado!");
	}
	
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		// cenario 
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123"));
		Assertions.assertThat(exception)
			.isInstanceOf(ErroAutenticacao.class)
			.hasMessage("Senha inválida!");
	}
	
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenario
//		Esta informando se tiver qualquer string no metodo existsByEmail deve retornar false para que consiga salvar o email.
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validarEmail("email@email.com");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenario 
//		Esta informando se tiver qualquer string no metodo existsByEmail deve retornar True para que não consiga salvar o email.
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//acao
		service.validarEmail("email@email.com");
		
		
	}

}
