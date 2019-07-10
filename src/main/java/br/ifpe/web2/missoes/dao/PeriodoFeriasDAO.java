package br.ifpe.web2.missoes.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifpe.web2.missoes.model.Funcionario;
import br.ifpe.web2.missoes.model.PeriodoFerias;

@Repository
public interface PeriodoFeriasDAO extends JpaRepository<PeriodoFerias, Integer> {

	Optional<PeriodoFerias> findByFuncionario(Funcionario funcionario);
	void deleteByFuncionario(Funcionario funcionario);
	boolean existsByFuncionario(Funcionario funcionario);

}
