package br.ifpe.web2.missoes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cargos")
public class Cargo {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 70, unique = true)
	@NotBlank(message = "Informe a descrição")
	@Size(max = 70, message = "A descrição deve ter no máximo {max} 70 caracteres")
	private String descricao;
	
	@Column(length = 15)
	@Size(max = 15, message = "A descrição abrevidada deve ter no máximo {max} caracteres")
	private String descricaoAbreviada;
	
	@Column(nullable = false)
	private Boolean ativo;

	
	public Cargo() {
		this.ativo = true;
	}
	public Cargo(String descricao, String descricaoAbreviada, Boolean ativo) {
		this.descricao = descricao;
		this.descricaoAbreviada = descricaoAbreviada;
		this.ativo = ativo;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}
	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
}