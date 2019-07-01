package br.ifpe.web2.missoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ifpe.web2.missoes.dao.EmpresaDAO;
import br.ifpe.web2.missoes.model.Empresa;

@Service
public class EmpresaService {

	@Autowired private EmpresaDAO empresas;
	
	public Empresa save(Empresa empresa) {
		return this.empresas.save(empresa);
	}
	public Optional<Empresa> findById(Integer id) {
		return this.empresas.findById(id);
	}
	public List<Empresa> findAll() {
		return this.empresas.findAll();
	}
	public List<Empresa> findFirst10ByOrderByNomeAsc() {
		return this.empresas.findFirst10ByOrderByNomeAsc();
	}
	public List<Empresa> findAllByNome(String nome) {
		return this.empresas.findAllByNome(nome);
	}
	public Optional<Empresa> findByNomeEquals(String nome) {
		return this.empresas.findByNomeEquals(nome);
	}
	public void deleteById(Integer id) {
		this.empresas.deleteById(id);
	}
	
	public boolean existe(Empresa empresa) {
		return this.empresas.existsByNome(empresa.getNome());
	}
	public List<Empresa> findAllByAtiva(boolean ativa) {
		return this.empresas.findAllByAtiva(ativa);
	}
	
}
