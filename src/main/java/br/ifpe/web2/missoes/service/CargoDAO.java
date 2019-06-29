package br.ifpe.web2.missoes.service;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifpe.web2.missoes.model.Cargo;

public interface CargoDAO extends JpaRepository<Cargo, Integer> {

}
