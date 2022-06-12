package com.renatom.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.renatom.minhasfinancas.model.entity.Lancamento;
import com.renatom.minhasfinancas.model.enums.StatusLancamento;
import com.renatom.minhasfinancas.model.enums.TipoLancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveSalvarLancamento() {

		Lancamento lancamento = criarLancamento();
		lancamento = repository.save(lancamento);

		Assertions.assertThat(lancamento.getId()).isNotNull();

	}

	@Test
	public void deveDeletarLancamento() {
		Lancamento lancamento = criaEPersisteUmLancamento();

		lancamento = entityManager.find(Lancamento.class, lancamento.getId());

		repository.delete(lancamento);

		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		Assertions.assertThat(lancamentoInexistente).isNull();

	}
	
	@Test
	public void deveAtualizarLancamento() {
		Lancamento lancamento = criaEPersisteUmLancamento();
		
		lancamento.setAno(2020);
		lancamento.setDescricao("Teste atualização");
		lancamento.setTipo(TipoLancamento.DESPESA);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2020);
		Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste atualização");
		Assertions.assertThat(lancamentoAtualizado.getTipo()).isEqualTo(TipoLancamento.DESPESA);
	

	}
	
	@Test
	public void deveBuscarPorId() {
		Lancamento lancamento = criaEPersisteUmLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
		
	}
	

	private Lancamento criaEPersisteUmLancamento() {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		return lancamento;
	}

	public static Lancamento criarLancamento() {
		return Lancamento.builder().descricao("lancamento qualquer").mes(1).ano(2019).valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA).status(StatusLancamento.EFETIVADO).build();
	}

}
