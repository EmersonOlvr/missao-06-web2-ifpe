package br.ifpe.web2.missoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ifpe.web2.missoes.dao.FuncionarioDAO;
import br.ifpe.web2.missoes.model.Cargo;
import br.ifpe.web2.missoes.model.Empresa;
import br.ifpe.web2.missoes.model.Funcionario;

@Service
public class FuncionarioService {

	@Autowired private FuncionarioDAO funcs;
	
	public List<Funcionario> findAllByCargo(Cargo cargo) {
		return this.funcs.findAllByCargo(cargo);
	}
	public List<Funcionario> findAllByEmpresa(Empresa empresa) {
		return this.funcs.findAllByEmpresa(empresa);
	}
	public boolean existsByCpf(String cpf) {
		return this.funcs.existsByCpf(cpf);
	}
	public Optional<Funcionario> findByCpf(String cpf) {
		return this.funcs.findByCpf(cpf);
	}
	
	public Funcionario salvar(Funcionario func) {
		return this.funcs.save(func);
	}
	
}
