package com.renatom.minhasfinancas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.renatom.minhasfinancas.dto.AtualizarStatusDTO;
import com.renatom.minhasfinancas.dto.LancamentoDTO;
import com.renatom.minhasfinancas.exception.RegraNegocioException;
import com.renatom.minhasfinancas.model.entity.Lancamento;
import com.renatom.minhasfinancas.model.entity.Usuario;
import com.renatom.minhasfinancas.model.enums.StatusLancamento;
import com.renatom.minhasfinancas.model.enums.TipoLancamento;
import com.renatom.minhasfinancas.service.LancamentoService;
import com.renatom.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

	private LancamentoService service;

	private UsuarioService usuarioService;

	public LancamentoController(LancamentoService service, UsuarioService usuarioService) {
		this.service = service;
		this.usuarioService = usuarioService;
	}

//	Envia os parametros para filtro com somente o idUsuario sendo obrigatorio.
//	Criando uma nova entidade de Lancamento com os filtros cadastrados e populado com os parametros recebidos.
//	Chamado o metodo de buscar por lancamento passando a entidade lancamentoFiltro com os filtros enviados no parametro
	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "ano", required = false) Integer ano, @RequestParam(value = "tipo", required = false) TipoLancamento tipo,
			@RequestParam(value = "mes", required = false) Integer mes, @RequestParam("usuario") Long idUsuario) {

		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setAno(ano);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setTipo(tipo);

		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if (usuario.isEmpty()) {
			return ResponseEntity.badRequest()
					.body("N??o foi possivel realizar a consulta. Usuario n??o encontrado na base de Dados.");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}

		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);

	}
	
	@GetMapping("/{id}")
	public ResponseEntity buscarLancamentoPorId(@PathVariable Long id) {
		return service.buscarPorId(id)
				.map(lancamento -> new ResponseEntity(converterLancamentoDto(lancamento), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
	}

//	Transforma a resposta de converterLancamento em uma variavel e tenta salvar a mesma , 
//	tambem salvando a resposta na mesma variavel para que possa retornar na requisi????o OK
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = converterLancamento(dto);
			entidade = service.salvar(entidade);
			return ResponseEntity.ok(entidade);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

//	Verifica se ha um lancamento para o id informado buscando pelo metodo buscarPorId e tenta atualizar com o lancamento passado no body, caso nao 
//	da um erro ao atualizar, e se nao achar lan??a um erro tambem 
	@PutMapping("/{id}")
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO dto) {
		return service.buscarPorId(id).map(entity -> {
			try {
				Lancamento lancamento = converterLancamento(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("Lan??amento n??o encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}

//	Realiza a atualiza????o do status do lancamento passando o id e o status
	@PutMapping("/{id}/atualiza-status")
	public ResponseEntity atualizaStatus(@PathVariable("id") Long id, @RequestBody AtualizarStatusDTO dto) {
		return service.buscarPorId(id).map(entidade -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());

			if (statusSelecionado == null) {
				return ResponseEntity.badRequest()
						.body("N??o foi possivel atualizar o status do lan??amento, envie um status v??lido");
			}

			try {
				entidade.setStatus(statusSelecionado);
				service.atualizar(entidade);
				return ResponseEntity.ok(entidade);
			} catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lan??amento n??o encontrado na base de Dados.", HttpStatus.BAD_REQUEST));

	}

//	Realizado o metodo de buscar por id para capturar o Lancamento e em seguida deletar.
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable Long id) {
		return service.buscarPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Lan??amento n??o encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
	
	private LancamentoDTO converterLancamentoDto(Lancamento lancamento) {
		return LancamentoDTO.builder()
				.id(lancamento.getId())
				.descricao(lancamento.getDescricao())
				.valor(lancamento.getValor())
				.ano(lancamento.getAno())
				.mes(lancamento.getMes())
				.tipo(lancamento.getTipo().name())
				.status(lancamento.getStatus().name())
				.usuario(lancamento.getUsuario().getId())
				.build();
	}

//	Metodo para converter o lancamento dto em lancamento
	private Lancamento converterLancamento(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());

//		Criado metodo para buscar usuario por id , caso nao encontre retornar uma regra de negocio exce????o 
		Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usu??rio n??o encontrado para o Id informado."));

		lancamento.setUsuario(usuario);

		if (dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}

		if (dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));

		}

		return lancamento;
	}
	
	

}
