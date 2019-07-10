package br.ifpe.web2.missoes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import br.ifpe.web2.missoes.dao.EnderecoDAO;
import br.ifpe.web2.missoes.dao.FotoDAO;
import br.ifpe.web2.missoes.dao.FuncionarioDAO;
import br.ifpe.web2.missoes.dao.PeriodoFeriasDAO;
import br.ifpe.web2.missoes.exceptions.FuncionarioNotFoundException;
import br.ifpe.web2.missoes.model.Cargo;
import br.ifpe.web2.missoes.model.Empresa;
import br.ifpe.web2.missoes.model.Endereco;
import br.ifpe.web2.missoes.model.Foto;
import br.ifpe.web2.missoes.model.Funcionario;
import br.ifpe.web2.missoes.model.PeriodoFerias;
import br.ifpe.web2.missoes.util.Util;
import br.ifpe.web2.missoes.util.exceptions.CPFException;

@Service
public class FuncionarioService {

	@Autowired private FuncionarioDAO funcs;
	@Autowired private EnderecoDAO enderecos;
	@Autowired private FotoDAO fotos;
	@Autowired private PeriodoFeriasDAO ferias;
	
	@Autowired private FotoStorageService fotoService;
	
	@Transactional // deve ser usada quando muitas operações são feitas no banco
	public Funcionario salvar(Funcionario funcionario) {
		if (funcionario.getId() == null) {
			Foto foto = funcionario.getFoto();
			Endereco end = funcionario.getEndereco();
			
			funcionario.setFoto(null);
			funcionario.setEndereco(null);
			
			// salva o funcionário no banco
			funcionario = this.funcs.save(funcionario);
			
			foto.setFuncionario(funcionario);
			end.setFuncionario(funcionario);
			
			Foto fotoSalva = fotoService.salvar(foto);
			Endereco enderecoSalvo = this.enderecos.save(end);
			
			funcionario.setFoto(fotoSalva);
			funcionario.setEndereco(enderecoSalvo);
		} else {
			PeriodoFerias pf = funcionario.getPeriodoFerias();
			if (pf != null && pf.getDataInicio() != null && pf.getDataFim() != null) {
				pf.setFuncionario(funcionario);
				funcionario.setPeriodoFerias(pf);
			} else {
				funcionario.setPeriodoFerias(null);
				this.funcs.save(funcionario);
				if (this.ferias.existsByFuncionario(funcionario)) {
					this.ferias.deleteByFuncionario(funcionario);
				}
			}
		}
		// atualiza o funcionário no banco
		return this.funcs.save(funcionario);
	}
	

	public Funcionario obterPorId(Integer id) throws FuncionarioNotFoundException {
		return this.funcs.findById(id).orElseThrow(() -> new FuncionarioNotFoundException("Funcionário inexistente."));
	}
	public Optional<Funcionario> obterPorMatricula(String matricula) {
		return this.funcs.findByMatricula(matricula);
	}

	public List<Funcionario> listarPrimeiros10OrdenadosPorDataAdmissao() {
		return this.funcs.findFirst10ByOrderByDataAdmissaoAsc();
	}
	public List<Funcionario> findAll(Example<Funcionario> exemploFuncionario) {
		return this.funcs.findAll(exemploFuncionario, Sort.by("nome"));
	}
	
	public boolean existeComCpf(String cpf) {
		return this.funcs.existsByCpf(cpf);
	}
	public boolean existeComEmpresa(Empresa empresa) {
		return this.funcs.existsByEmpresa(empresa);
	}
	public boolean existeComCargo(Cargo cargo) {
		return this.funcs.existsByCargo(cargo);
	}
	
	@Transactional
	public void deletarPorId(Integer id) throws FuncionarioNotFoundException {
		Funcionario funcionario = this.obterPorId(id);
		
		Integer endId = funcionario.getEndereco().getId();
		String fotoId = funcionario.getFoto().getId();
		
		funcionario.setEndereco(null);
		funcionario.setFoto(null);
		funcionario.setPeriodoFerias(null);

		this.salvar(funcionario);
		
		this.enderecos.deleteById(endId);
		this.fotos.deleteById(fotoId);
		if (this.ferias.existsByFuncionario(funcionario)) {
			this.ferias.deleteByFuncionario(funcionario);
		}
		
		this.funcs.deleteById(id);
	}
	
	public ArrayList<String> validar(Funcionario funcionario) {
		ArrayList<String> erros = new ArrayList<String>();
		int id = funcionario.getId() != null ? funcionario.getId() : 0;
		Optional<Funcionario> func = this.funcs.findById(id);
		boolean jaCadastrado = func.isPresent() ? true : false;
		
		// ====== Validação da Matrícula ====== //
		if (!jaCadastrado || jaCadastrado && !funcionario.getMatricula().equals(func.get().getMatricula())) {
			if (this.funcs.existsByMatricula(funcionario.getMatricula())) {
				erros.add("Matrícula já informada para funcionário " 
						+ this.funcs.findByMatricula(funcionario.getMatricula()).get().getId());
			}
		}
		
		// ====== Validação do CPF ====== //
		if (!jaCadastrado || jaCadastrado && !funcionario.getCpf().equals(func.get().getCpf())) {
			if (!Strings.isBlank(funcionario.getCpf())) {
				String cpfAntesDaFormatacao = funcionario.getCpf();
				try {
					// tira os pontos e o hífen do CPF
					funcionario.setCpf(funcionario.getCpf().replaceAll("[\\.\\-]+", ""));
					
					Util.validarCPF(funcionario.getCpf());
					
					funcionario.setCpf(Util.formatarCPF(funcionario.getCpf()));
				} catch (CPFException e) {
					funcionario.setCpf(cpfAntesDaFormatacao);
					erros.add(e.getMessage());
				}
				if (this.funcs.existsByCpf(funcionario.getCpf())) {
					erros.add("CPF já informado para funcionário "+funcs.findByCpf(funcionario.getCpf()).get().getId());
				}
			}
		}
		
		// ====== Validação da Data de Nascimento ====== //
		if (funcionario.getDataNascimento() != null && 
			funcionario.getDataNascimento().plusYears(18).isAfter(LocalDate.now())) 
		{
			erros.add("O funcionário terá que possuir, no mínimo, 18 anos de idade");
		}
		
		// ====== Validação do Endereço ====== //
		if (funcionario.getEndereco() == null) {
			erros.add("Informe os dados do Endereço");
		}
		if (funcionario.getEndereco().getEstado() == null) {
			erros.add("Informe o Estado");
		}
		if (Strings.isBlank(funcionario.getEndereco().getCidade())) {
			erros.add("Informe a Cidade");
		}
		if (Strings.isBlank(funcionario.getEndereco().getBairro())) {
			erros.add("Informe o Bairro");
		}
		if (funcionario.getEndereco().getNumero() == null) {
			erros.add("Informe o Número");
		}
		
		// ====== Validação do Período de Férias ====== //
		if (jaCadastrado && funcionario.getPeriodoFerias() != null) {
			PeriodoFerias pf = funcionario.getPeriodoFerias();
			if (pf.getDataInicio() != null && pf.getDataFim() == null) {
				erros.add("Informe a Data do Fim do Período de Ferias");
			} else if (pf.getDataFim() != null && pf.getDataInicio() == null) {
				erros.add("Informe a Data do Início do Período de Ferias");
			}
		}
		
		// ====== Validação da Senha ====== //
		if (jaCadastrado && funcionario.getSenha().isEmpty() ) {
			funcionario.setSenha(this.funcs.findById(funcionario.getId()).get().getSenha());
		}
		if (!jaCadastrado && Strings.isBlank(funcionario.getSenha())) {
			erros.add("Informe a Senha");
		}
		funcionario.setSenha(BCrypt.hashpw(funcionario.getSenha(), BCrypt.gensalt(12)));
		
		return erros;
	}
	
}
