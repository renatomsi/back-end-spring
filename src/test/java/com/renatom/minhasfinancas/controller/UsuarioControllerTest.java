package com.renatom.minhasfinancas.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renatom.minhasfinancas.dto.UsuarioDTO;
import com.renatom.minhasfinancas.exception.ErroAutenticacao;
import com.renatom.minhasfinancas.exception.RegraNegocioException;
import com.renatom.minhasfinancas.model.entity.Usuario;
import com.renatom.minhasfinancas.service.LancamentoService;
import com.renatom.minhasfinancas.service.UsuarioService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;

	@Autowired
	MockMvc mvc;

	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService lancamentoService;

	@Test
	public void deveAutenticarUmUsuario() throws Exception {
//		cenario
		String email = "email@email.com";
		String senha = "123";

		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();

		Mockito.when(service.autenticar(email, senha)).thenReturn(usuario);

//		Ira criar uma string com formato de json com o usuario dto
		String json = new ObjectMapper().writeValueAsString(dto);

//		Execução e verificaçãoS
//		Simula uma requisição post aceitando um formato de JSON e enviando no body um json com os dados que recebeu do dto
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/auth")).accept(JSON)
				.contentType(JSON).content(json);

//		Envia a requisição e espera que o retorno seja status OK e compara o corpo do retorno se bate as informações,
//		foi alterado no dto para nao exibir a senha no retorno
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("name").value(usuario.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));

	}
	
	@Test
	public void deveRetornarUmBadRequestAoAutenticarUsuario() throws Exception {
//		cenario
		String email = "email@email.com";
		String senha = "123";

		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();

		Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);

//		Ira criar uma string com formato de json com o usuario dto
		String json = new ObjectMapper().writeValueAsString(dto);

//		Execução e verificaçãoS
//		Simula uma requisição post aceitando um formato de JSON e enviando no body um json com os dados que recebeu do dto
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/auth")).accept(JSON)
				.contentType(JSON).content(json);

//		Envia a requisição e espera que o retorno seja status OK e compara o corpo do retorno se bate as informações,
//		foi alterado no dto para nao exibir a senha no retorno
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());

	}
	
	@Test
	public void deveSalvarUmUsuarioNovo() throws Exception {
//		cenario
		String email = "email@email.com";
		String senha = "123";

		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();

		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

//		Ira criar uma string com formato de json com o usuario dto
		String json = new ObjectMapper().writeValueAsString(dto);

//		Execução e verificaçãoS
//		Simula uma requisição post aceitando um formato de JSON e enviando no body um json com os dados que recebeu do dto
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(JSON)
				.contentType(JSON).content(json);

//		Envia a requisição e espera que o retorno seja status OK e compara o corpo do retorno se bate as informações,
//		foi alterado no dto para nao exibir a senha no retorno
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated());

	}
	
	
	@Test
	public void deveRetornarBadRequestAoSalvarUsuarioInvalido() throws Exception {
//		cenario
		String email = "email@email.com";
		String senha = "123";

		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();

		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
//		Ira criar uma string com formato de json com o usuario dto
		String json = new ObjectMapper().writeValueAsString(dto);

//		Execução e verificaçãoS
//		Simula uma requisição post aceitando um formato de JSON e enviando no body um json com os dados que recebeu do dto
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(JSON)
				.contentType(JSON).content(json);

//		Envia a requisição e espera que o retorno seja status OK e compara o corpo do retorno se bate as informações,
//		foi alterado no dto para nao exibir a senha no retorno
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());

	}
	
	
}
