package br.ifpe.web2.missoes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ifpe.web2.missoes.model.Funcionario;
import br.ifpe.web2.missoes.service.CargoDAO;
import br.ifpe.web2.missoes.service.FuncionarioService;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired private FuncionarioService funcionarioService;
	
	@Autowired private EmpresaDAO empresas;
	@Autowired private CargoDAO cargos;

	@GetMapping("/adicionar")
	public ModelAndView viewAdicionarFuncionario() {
		ModelAndView mv = new ModelAndView("/funcionario-adicionar");
		mv.addObject("funcionario", new Funcionario());
		mv.addObject("cargos", cargos.findAll());
		mv.addObject("empresas", empresas.findAll());
		return mv;
	}
	
	@PostMapping("/adicionar")
	public String adicionarFuncionario(@ModelAttribute Funcionario funcionario, RedirectAttributes ra) {
		funcionarioService.salvar(funcionario);
		ra.addFlashAttribute("msgSucesso", "Funcionário salvo com sucesso!");
		return "redirect:/funcionarios/adicionar";
	}
	
}
