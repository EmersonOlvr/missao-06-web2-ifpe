package br.ifpe.web2.missoes.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ifpe.web2.missoes.dao.DepartamentoDAO;
import br.ifpe.web2.missoes.model.Departamento;

@Service
public class DepartamentoService {

	@Autowired private DepartamentoDAO departamentos;
	
	@Autowired private FuncionarioService funcService;
	
	public Departamento salvar(@Valid Departamento departamento) {
		return this.departamentos.save(departamento);
	}
	
	public Optional<Departamento> obterPorId(Integer id) {
		return this.departamentos.findById(id);
	}

	public List<Departamento> listarTodos() {
		return this.departamentos.findAll();
	}
	public List<Departamento> listarTodosPorNome(String q) {
		return this.departamentos.findAllByNome(q);
	}
	public List<Departamento> listarPrimeiros10OrdenadasPorNome() {
		return this.departamentos.findFirst10ByOrderByNomeAsc();
	}
	
	public boolean existe(Departamento departamento) {
		return this.departamentos.existsByNome(departamento.getNome());
	}
	
	public void deletarPorId(Integer id) throws Exception {
		Departamento dep = this.departamentos.findById(id).orElseThrow(() -> new Exception("Departamento inexistente."));
		if (this.funcService.existeComDepartamento(dep)) {
			throw new Exception("Não é possível excluir o(s) departamento(s) selecionado(s) devido a vínculos com outras informações.");
		}
		
		this.departamentos.deleteById(id);
	}
	
}
