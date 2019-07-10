package br.ifpe.web2.missoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ifpe.web2.missoes.dao.CargoDAO;
import br.ifpe.web2.missoes.model.Cargo;

@Service
public class CargoService {

	@Autowired private CargoDAO cargos;
	
	@Autowired private FuncionarioService funcService;
	
	public Cargo salvar(Cargo cargo) {
		return this.cargos.save(cargo);
	}
	
	public Optional<Cargo> obterPorId(Integer id) {
		return this.cargos.findById(id);
	}
	public Optional<Cargo> obterPorDescricao(String descricao) {
		return this.cargos.findByDescricaoEquals(descricao);
	}
	
	public List<Cargo> listarTodos() {
		return this.cargos.findAll();
	}
	public List<Cargo> listarTodosAtivos() {
		return this.cargos.findAllByAtivo(true);
	}
	public List<Cargo> listarPrimeiros10OrdenadosPorDescricao() {
		return this.cargos.findFirst10ByOrderByDescricaoAsc();
	}
	public List<Cargo> listarTodosPorDescricao(String descricao) {
		return this.cargos.findAllByDescricao(descricao);
	}
	
	public boolean existe(Cargo cargo) {
		return this.cargos.existsByDescricao(cargo.getDescricao());
	}
	
	public void deletarPorId(Integer id) throws Exception {
		Cargo cargo = this.cargos.findById(id).orElseThrow(() -> new Exception("Cargo inexistente."));
		if (this.funcService.existeComCargo(cargo)) {
			throw new Exception("Não é possível excluir o(s) cargo(s) de funcionário"
					+ " selecionado(s) devido a vínculos com outras informações.");
		}
		
		this.cargos.deleteById(id);
	}
	
}
