package br.ifpe.web2.missoes.controller;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifpe.web2.missoes.model.Empresa;

public interface EmpresaDAO extends JpaRepository<Empresa, Integer> {

}
