package br.ifpe.web2.missoes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.ifpe.web2.missoes.enums.EstadosEnum;

@Entity
@Table(name = "enderecos")
public class Endereco {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(optional = false)
	private Funcionario funcionario;
	
	@Column(columnDefinition = "char(2)", nullable = false)
	@Enumerated(EnumType.STRING)
	private EstadosEnum estado;
	
	@Column(nullable = false, length = 50)
	private String cidade;
	
	@Column(nullable = false, length = 50)
	private String bairro;
	
	@Column(nullable = false)
	private Integer numero;
	
	@Column(length = 30)
	private String complemento;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public EstadosEnum getEstado() {
		return estado;
	}
	public void setEstado(EstadosEnum estado) {
		this.estado = estado;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	
	@Override
	public String toString() {
		return "Endereco [id=" + id + ", funcionario=" + (funcionario != null ? funcionario.getId() : funcionario) + ", estado=" + estado + ", cidade=" + cidade
				+ ", bairro=" + bairro + ", numero=" + numero + ", complemento=" + complemento + "]";
	}
	
}
