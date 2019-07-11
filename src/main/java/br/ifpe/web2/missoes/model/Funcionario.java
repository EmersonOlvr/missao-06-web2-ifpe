package br.ifpe.web2.missoes.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 8, unique = true)
	@NotBlank(message = "Informe a Matrícula")
	@Size(max = 8, message = "A matrícula deve ter no máximo {max} dígitos")
	private String matricula;
	
	@Column(nullable = false, length = 70)
	@NotBlank(message = "Informe o Nome")
	@Size(max = 70, message = "O nome deve ter no máximo {max} caracteres")
	private String nome;
	
	@Column(nullable = false, length = 14, unique = true)
	@NotBlank(message = "Informe o CPF")
	@Size(max = 14, message = "O CPF deve ter no máximo {max} caracteres")
	private String cpf;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Informe a Data de Nascimento")
	private LocalDate dataNascimento;
	
	@ManyToOne(optional = false)
	@NotNull(message = "Informe o Cargo")
	private Cargo cargo;
	
	@ManyToOne(optional = false)
	@NotNull(message = "Informe a Empresa")
	private Empresa empresa;
	
	@Column(nullable = false)
	@NotNull(message = "Informe o salário")
	private BigDecimal salario;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Informe a Data de Admissão")
	private LocalDate dataAdmissao;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataDemissao;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Endereco endereco;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Foto foto;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Departamento departamento;
	
	@OneToOne(cascade = CascadeType.ALL)
	private PeriodoFerias periodoFerias;
	
	@Column(length = 70)
	private String senha;
	
	
	public Funcionario() {}
	public Funcionario(
			String matricula, String nome, String cpf, LocalDate dataNascimento, Cargo cargo, 
			Empresa empresa, BigDecimal salario, LocalDate dataAdmissao, LocalDate dataDemissao,
			Endereco endereco, Departamento departamento, PeriodoFerias periodoFerias) {
		this.matricula = matricula;
		this.nome = nome;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.cargo = cargo;
		this.empresa = empresa;
		this.salario = salario;
		this.dataAdmissao = dataAdmissao;
		this.dataDemissao = dataDemissao;
		this.endereco = endereco;
		this.departamento = departamento;
		this.periodoFerias = periodoFerias;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
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
	public BigDecimal getSalario() {
		return salario;
	}
	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}
	public LocalDate getDataAdmissao() {
		return dataAdmissao;
	}
	public void setDataAdmissao(LocalDate dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}
	public LocalDate getDataDemissao() {
		return dataDemissao;
	}
	public void setDataDemissao(LocalDate dataDemissao) {
		this.dataDemissao = dataDemissao;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public Foto getFoto() {
		return foto;
	}
	public void setFoto(Foto foto) {
		this.foto = foto;
	}
	public Departamento getDepartamento() {
		return departamento;
	}
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
	public PeriodoFerias getPeriodoFerias() {
		return periodoFerias;
	}
	public void setPeriodoFerias(PeriodoFerias periodoFerias) {
		this.periodoFerias = periodoFerias;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	@Override
	public String toString() {
		return "Funcionario [id=" + id + ", matricula=" + matricula + ", nome=" + nome + ", cpf=" + cpf
				+ ", dataNascimento=" + dataNascimento + ", cargo=" + cargo + ", empresa=" + empresa + ", dataAdmissao="
				+ dataAdmissao + ", dataDemissao=" + dataDemissao + ", departamento=" + departamento + ", senha=" + senha + "]";
	}
	
}
