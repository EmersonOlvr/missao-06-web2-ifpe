package br.ifpe.web2.missoes.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.ifpe.web2.missoes.model.Cargo;
import br.ifpe.web2.missoes.model.Departamento;
import br.ifpe.web2.missoes.model.Empresa;
import br.ifpe.web2.missoes.model.Funcionario;

@Repository
public interface FuncionarioDAO extends JpaRepository<Funcionario, Integer> {

	@Query("SELECT f FROM Funcionario f WHERE f.cargo = :cargo")
	public List<Funcionario> findAllByCargo(Cargo cargo);
	
	@Query("SELECT f FROM Funcionario f WHERE f.empresa = :empresa")
	public List<Funcionario> findAllByEmpresa(Empresa empresa);
	
	public List<Funcionario> findFirst10ByOrderByDataAdmissaoAsc();
	
	public boolean existsByCpf(String cpf);
	public boolean existsByMatricula(String matricula);
	public boolean existsByEmpresa(Empresa empresa);
	public boolean existsByCargo(Cargo cargo);
	public boolean existsByDepartamento(Departamento departamento);
	
	public Optional<Funcionario> findByCpf(String cpf);
	public Optional<Funcionario> findByMatricula(String matricula);
	
}
