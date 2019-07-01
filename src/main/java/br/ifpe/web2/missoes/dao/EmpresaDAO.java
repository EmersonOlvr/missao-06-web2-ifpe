package br.ifpe.web2.missoes.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.ifpe.web2.missoes.model.Empresa;

public interface EmpresaDAO extends JpaRepository<Empresa, Integer> {

	public List<Empresa> findFirst10ByOrderByNomeAsc();
	
	@Query("SELECT e FROM Empresa e WHERE e.nome LIKE %:nome% OR e.nomeAbreviado LIKE %:nome%")
	public List<Empresa> findAllByNome(String nome);
	
	public Optional<Empresa> findByNomeEquals(String nome);
	
	public boolean existsByNome(String nome);
	
	@Query("SELECT e FROM Empresa e WHERE e.ativa = :ativa")
	public List<Empresa> findAllByAtiva(boolean ativa);
	
}
