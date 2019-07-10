package br.ifpe.web2.missoes.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifpe.web2.missoes.model.Foto;
import br.ifpe.web2.missoes.model.Funcionario;

public interface FotoDAO extends JpaRepository<Foto, String> {

	boolean existsByFuncionario(Funcionario funcionario);
	Optional<Foto> findByFuncionario(Funcionario funcionario);	
	
}
