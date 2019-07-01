package br.ifpe.web2.missoes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "empresas")
public class Empresa {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 50)
	@NotBlank(message = "Informe o nome")
	@Size(max = 50, message = "O nome deve ter no máximo {max} caracteres")
	private String nome;
	
	@Column(length = 10)
	@Size(max = 10, message = "O nome abreviado deve ter no máximo {max} caracteres")
	private String nomeAbreviado;
	
	@Column(length = 80)
	@Size(max = 80, message = "O e-mail deve ter no máximo {max} caracteres")
	private String email;
	
	@Column(nullable = false)
	@NotNull(message = "Informe se a empresa é principal")
	private Boolean principal;
	
	@Column(nullable = false)
	@NotNull(message = "Informe a situação")
	private Boolean ativa;
	
	
	public Empresa() {
		this.principal = true;
		this.ativa = true;
	}
	public Empresa(String nome, String nomeAbreviado, String email, Boolean principal, Boolean ativa) {
		this.nome = nome;
		this.nomeAbreviado = nomeAbreviado;
		this.email = email;
		this.principal = principal;
		this.ativa = ativa;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNomeAbreviado() {
		return nomeAbreviado;
	}
	public void setNomeAbreviado(String nomeAbreviado) {
		this.nomeAbreviado = nomeAbreviado;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getPrincipal() {
		return principal;
	}
	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}
	public Boolean getAtiva() {
		return ativa;
	}
	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}
	
}
