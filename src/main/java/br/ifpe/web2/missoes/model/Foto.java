package br.ifpe.web2.missoes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "fotos")
public class Foto {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	@ManyToOne(optional = false)
	private Funcionario funcionario;

	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String tipo;
	
	@Column(nullable = false)
	@Lob
	private byte[] conteudo;

	
	public Foto() {}
	public Foto(Funcionario funcionario, String nome, String tipo, byte[] conteudo) {
		this.funcionario = funcionario;
		this.nome = nome;
		this.tipo = tipo;
		this.conteudo = conteudo;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public byte[] getConteudo() {
		return conteudo;
	}
	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}
	
	
	@Override
	public String toString() {
		return "Foto [id=" + id + ", funcionario=" + (funcionario != null ? funcionario.getId() : funcionario) + ", nome=" + nome + 
				", tipo=" + tipo + ", conteudo = "+ (conteudo != null ? "Sim" : "Não") + "]";
	}

}
