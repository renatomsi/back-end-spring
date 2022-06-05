package com.renatom.minhasfinancas.service;

import java.util.List;

import com.renatom.minhasfinancas.model.entity.Lancamento;
import com.renatom.minhasfinancas.model.enums.StatusLancamento;


public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);

}
