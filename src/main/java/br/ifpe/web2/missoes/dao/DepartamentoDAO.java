package br.ifpe.web2.missoes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.ifpe.web2.missoes.model.Departamento;

public interface DepartamentoDAO extends JpaRepository<Departamento, Integer> {

	@Query("SELECT d FROM Departamento d WHERE d.nome LIKE %:nome%")
	public List<Departamento> findAllByNome(String nome);

	public List<Departamento> findFirst10ByOrderByNomeAsc();

	public boolean existsByNome(String nome);
	
}
