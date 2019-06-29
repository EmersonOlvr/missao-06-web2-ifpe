package br.ifpe.web2.missoes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ifpe.web2.missoes.dao.FuncionarioDAO;
import br.ifpe.web2.missoes.model.Funcionario;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioDAO funcs;
	
	public void salvar(Funcionario func) {
		this.funcs.save(func);
	}
	
}
