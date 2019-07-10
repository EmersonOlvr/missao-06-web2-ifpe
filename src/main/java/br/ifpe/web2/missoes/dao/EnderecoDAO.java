package br.ifpe.web2.missoes.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifpe.web2.missoes.model.Endereco;
import br.ifpe.web2.missoes.model.Funcionario;

@Repository
public interface EnderecoDAO extends JpaRepository<Endereco, Integer> {

	Optional<Endereco> findByFuncionario(Funcionario funcionario);

}
