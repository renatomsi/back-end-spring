package com.renatom.minhasfinancas.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renatom.minhasfinancas.model.entity.Lancamento;
import com.renatom.minhasfinancas.model.enums.StatusLancamento;
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
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
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

	
}