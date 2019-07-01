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
	
	public Cargo save(Cargo cargo) {
		return this.cargos.save(cargo);
	}
	public Optional<Cargo> findById(Integer id) {
		return this.cargos.findById(id);
	}
	public List<Cargo> findAll() {
		return this.cargos.findAll();
	}
	public List<Cargo> findFirst10ByOrderByDescricaoAsc() {
		return this.cargos.findFirst10ByOrderByDescricaoAsc();
	}
	public List<Cargo> findAllByDescricao(String descricao) {
		return this.cargos.findAllByDescricao(descricao);
	}
	public Optional<Cargo> findByDescricaoEquals(String descricao) {
		return this.cargos.findByDescricaoEquals(descricao);
	}
	public void deleteById(Integer id) {
		this.cargos.deleteById(id);
	}
	public List<Cargo> findAllByAtivo(boolean ativo) {
		return this.cargos.findAllByAtivo(ativo);
	}
	
	public boolean existe(Cargo cargo) {
		return this.cargos.existsByDescricao(cargo.getDescricao());
	}
	
}
