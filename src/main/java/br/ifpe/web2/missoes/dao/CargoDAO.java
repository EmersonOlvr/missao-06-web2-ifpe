package br.ifpe.web2.missoes.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.ifpe.web2.missoes.model.Cargo;

@Repository
public interface CargoDAO extends JpaRepository<Cargo, Integer> {

	public List<Cargo> findFirst10ByOrderByDescricaoAsc();
	
	@Query("SELECT c FROM Cargo c WHERE c.descricao LIKE %:descricao% OR c.descricaoAbreviada LIKE %:descricao%")
	public List<Cargo> findAllByDescricao(String descricao);
	
	public Optional<Cargo> findByDescricaoEquals(String descricao);
	
	public boolean existsByDescricao(String descricao);
	
	@Query("SELECT c FROM Cargo c WHERE c.ativo = :ativo")
	public List<Cargo> findAllByAtivo(boolean ativo);
	
}
