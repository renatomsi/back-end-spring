package com.renatom.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.renatom.minhasfinancas.model.enums.StatusLancamento;
import com.renatom.minhasfinancas.model.enums.TipoLancamento;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "lancamento", schema = "fincancas")
@Data
@Builder
public class Lancamento {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "descicao")
	private String descricao;
	
	@Column(name = "mes")
	private Integer mes;
	
	@Column(name = "ano")
	private Integer ano;

	// Para relacionamento de tabelas: Many= muitos lancamentos toOne= para um usuario
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario id_usuario;
	
	@Column(name ="valor")
	private BigDecimal valor;
	
	@Column(name = "data_cadastro")
	// Converter o localDate 
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate dataCadastro;
	
	@Column(name = "tipo")
	// Informa que o valor salvo vem de um enum, se for EnumType.String vai salvar o valor que esta em string
	// Se for EnumType.Ordinal ira salvar a ordem no caso 0 para RECEITA e 1 para DESPESA , porem se trocar a
	// ordem pode da problema no banco de dados
	@Enumerated(value = EnumType.STRING)
	private TipoLancamento tipo;
	
	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private StatusLancamento status;

	

}