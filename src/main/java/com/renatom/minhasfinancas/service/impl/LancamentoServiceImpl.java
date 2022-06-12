package com.renatom.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renatom.minhasfinancas.exception.RegraNegocioException;
import com.renatom.minhasfinancas.model.entity.Lancamento;
import com.renatom.minhasfinancas.model.enums.StatusLancamento;
import com.renatom.minhasfinancas.model.enums.TipoLancamento;
import com.renatom.minhasfinancas.model.repository.LancamentoRepository;
import com.renatom.minhasfinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
//	Informa que a transação é somente leitura, fazendo assim algumas otimizações
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
//		Passando esse Example de 'org.springframework.data.domain' o repository ira fazer uma busca pela o conteudo pesquisado,
//		se o usuario cadastrou uma descrição ele ira buscar por uma descrição que contem a busca e ignorando caixa alta ou baixa.
		Example example = Example.of(lancamentoFiltro, 
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING));

//		Ira retornar todos os lancamentos que se encaixam nessa busca.
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validar(Lancamento lancamento) {
		
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descrição válida.");
		}
		
		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido.");
		}
		
		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4 ) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}
		
		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um Usuário");
		}
		
//		Metodo compare To compara se o getValor é maior que zero: sim => retorna 1 , igual => retorna 0 , menor => retorna -1
		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um Valor válido.");
		}
		
		if (lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de Lançamento.");
		}
		
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {
		return repository.findById(id);
	}

//	Capturando a soma das receitas e despesas para realizar a subtração 
	@Override
	public BigDecimal obterSaldoPorUsuario(Long id) {
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		
		if (receitas == null) {
			receitas = BigDecimal.ZERO;
		}
		
		if (despesas == null) {
			despesas = BigDecimal.ZERO;
		}

		return receitas.subtract(despesas);
	}

	
}
