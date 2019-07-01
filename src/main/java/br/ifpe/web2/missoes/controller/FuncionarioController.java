package br.ifpe.web2.missoes.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.ifpe.web2.missoes.model.Funcionario;
import br.ifpe.web2.missoes.service.CargoService;
import br.ifpe.web2.missoes.service.EmpresaService;
import br.ifpe.web2.missoes.service.FuncionarioService;
import br.ifpe.web2.missoes.util.ValidaCPF;
import br.ifpe.web2.missoes.util.exceptions.CPFException;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired private FuncionarioService funcionarioService;
	@Autowired private EmpresaService empresaService;
	@Autowired private CargoService cargoService;

	@GetMapping("/inserir")
	public ModelAndView viewInserirFuncionario() {
		ModelAndView mv = new ModelAndView("/funcionario-inserir");
		mv.addObject("funcionario", new Funcionario());
		mv.addObject("cargos", this.cargoService.findAllByAtivo(true));
		mv.addObject("empresas", this.empresaService.findAllByAtiva(true));
		return mv;
	}
	
	@PostMapping("/inserir")
	public ModelAndView inserirFuncionario(@Valid Funcionario funcionario, BindingResult br, Model model) {
		ModelAndView mv = new ModelAndView("/funcionario-inserir");
		mv.addObject("cargos", this.cargoService.findAllByAtivo(true));
		mv.addObject("empresas", this.empresaService.findAllByAtiva(true));
		
		if (!br.hasFieldErrors("cpf")) {
			String cpfAntesDaFormatacao = funcionario.getCpf();
			try {// tira os pontos e o hífen do CPF
				funcionario.setCpf(funcionario.getCpf().replaceAll("[\\.\\-]+", ""));
				
				ValidaCPF.validarCPF(funcionario.getCpf());
				
				funcionario.setCpf(ValidaCPF.formatarCPF(funcionario.getCpf()));
			} catch (CPFException e) {
				br.addError(new ObjectError("cpfInvalido", e.getMessage()));
				funcionario.setCpf(cpfAntesDaFormatacao);
			}
		}
		if (this.funcionarioService.existsByCpf(funcionario.getCpf())) {
			br.addError(new ObjectError("cpfExistente", "CPF já informado para funcionário "+
					funcionarioService.findByCpf(funcionario.getCpf()).get().getId()));
		}
		if (funcionario.getDataNascimento() != null && 
				funcionario.getDataNascimento().plusYears(18).isAfter(LocalDate.now())) 
		{
			br.addError(new ObjectError("menorDeIdade", "O funcionário terá que possuir, no mínimo, 18 anos de idade"));
		}
		if (br.hasErrors()) {
			return mv;
		}
		
		this.funcionarioService.salvar(funcionario);
		mv.addObject("msgSucesso", "Funcionário salvo com sucesso!");
		mv.addObject("funcionario", new Funcionario());
		
		return mv;
	}
	
}
