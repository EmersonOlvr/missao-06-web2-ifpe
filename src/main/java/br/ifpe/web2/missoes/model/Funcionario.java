package br.ifpe.web2.missoes.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 8)
	@NotBlank(message = "Informe a matrícula")
	@Max(value = 8, message = "A matrícula deve ter no máximo 8 dígitos")
	private Integer matricula;
	
	@Column(nullable = false, length = 70)
	@NotBlank(message = "Informe o nome")
	@Size(max = 70, message = "O nome deve ter no máximo {max} caracteres")
	private String nome;
	
	@Column(nullable = false, length = 11)
	@NotBlank(message = "Informe o CPF")
	@Size(max = 11, message = "O CPF deve ter no máximo {max} dígitos")
	private String cpf;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataNascimento;
	
	@OneToOne(optional = false)
	@NotNull(message = "Informe o cargo")
	private Cargo cargo;
	
	@ManyToOne(optional = false)
	@NotNull(message = "Informe a empresa")
	private Empresa empresa;
	
	
	public Funcionario() {}
	public Funcionario(Integer matricula, String nome, String cpf, LocalDate dataNascimento, Cargo cargo,
			Empresa empresa) {
		this.matricula = matricula;
		this.nome = nome;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.cargo = cargo;
		this.empresa = empresa;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMatricula() {
		return matricula;
	}
	public void setMatricula(Integer matricula) {
		this.matricula = matricula;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public Cargo getCargo() {
		return cargo;
	}
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
}
