package br.ifpe.web2.missoes.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class PeriodoFerias {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY )
	private Integer id;
	
	@OneToOne(optional = false)
	private Funcionario funcionario;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataInicio;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataFim;
	
	
	public PeriodoFerias() {}
	public PeriodoFerias(Funcionario funcionario, LocalDate dataInicio, LocalDate dataFim) {
		this.funcionario = funcionario;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
	}
	
	
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
	public LocalDate getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}
	public LocalDate getDataFim() {
		return dataFim;
	}
	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}
	
	
	@Override
	public String toString() {
		return "PeriodoFerias [id=" + id + ", funcionario=" + (funcionario != null ? funcionario.getId() : funcionario) + ", dataInicio=" + dataInicio + ", dataFim="
				+ dataFim + "]";
	}
	
}
