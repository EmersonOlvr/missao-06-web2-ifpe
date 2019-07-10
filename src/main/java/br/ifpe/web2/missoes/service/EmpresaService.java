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
	
	@Autowired private FuncionarioService funcService;
	
	public Empresa salvar(Empresa empresa) {
		return this.empresas.save(empresa);
	}
	
	public Optional<Empresa> obterPorId(Integer id) {
		return this.empresas.findById(id);
	}
	public Optional<Empresa> obterPorNome(String nome) {
		return this.empresas.findByNomeEquals(nome);
	}
	
	public List<Empresa> listarTodas() {
		return this.empresas.findAll();
	}
	public List<Empresa> listarTodasAtivas() {
		return this.empresas.findAllByAtiva(true);
	}
	public List<Empresa> listarPrimeiras10OrdenadasPorNome() {
		return this.empresas.findFirst10ByOrderByNomeAsc();
	}
	public List<Empresa> listarTodasPorNome(String nome) {
		return this.empresas.findAllByNome(nome);
	}

	public boolean existe(Empresa empresa) {
		return this.empresas.existsByNome(empresa.getNome());
	}
	public boolean existePrincipal() {
		return this.empresas.existsByPrincipal(true);
	}
	
	public void deletarPorId(Integer id) throws Exception {
		Empresa emp = this.empresas.findById(id).orElseThrow(() -> new Exception("Empresa inexistente."));
		if (this.funcService.existeComEmpresa(emp)) {
			throw new Exception("Não é possível excluir a(s) empresa(s) selecionada(s) devido a vínculos com outras informações.");
		}
		
		this.empresas.deleteById(id);
	}
	
	
}
