package br.ifpe.web2.missoes.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ifpe.web2.missoes.model.Departamento;
import br.ifpe.web2.missoes.service.DepartamentoService;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {

	@Autowired private DepartamentoService depService;
	
	@GetMapping("/")
	public ModelAndView viewListarDepartamentos(@RequestParam(required = false) String q) {
		ModelAndView mv = new ModelAndView("/departamento-listar");
		if (q != null && !q.isEmpty()) {
			List<Departamento> departamentos = this.depService.listarTodosPorNome(q);
			if (departamentos.size() == 1) {
				mv.setViewName("redirect:/departamentos/editar/"+departamentos.get(0).getId());
			} else {
				mv.addObject("departamentos", departamentos);
			}
			return mv;
		}
		
		mv.addObject("departamentos", this.depService.listarPrimeiros10OrdenadasPorNome());
		return mv;
	}
	
	@GetMapping("/inserir")
	public ModelAndView viewInserirDepartamento(Model model) {
		ModelAndView mv = new ModelAndView("/departamento-inserir");
		model.addAttribute("titulo", "Inserir Departamento");
		
		mv.addObject("departamento", new Departamento());
		return mv;
	}
	
	@PostMapping("/inserir")
	public ModelAndView inserirDepartamento(@Valid Departamento departamento, BindingResult br, Model model) {
		ModelAndView mv = new ModelAndView("/departamento-inserir");
		model.addAttribute("titulo", "Inserir Departamento");
		
		if (this.depService.existe(departamento)) {
			br.addError(new ObjectError("departamentoNomeExistente", "Já existe departamento com o nome informado"));
		}
		if (br.hasErrors()) {
			return mv;
		}
		
		this.depService.salvar(departamento);
		mv.addObject("msgSucesso", "Departamento salvo com sucesso!");
		mv.addObject("departamento", new Departamento());
		
		return mv;
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView viewAtualizarDepartamento(Model model, @PathVariable Integer id) {
		ModelAndView mv = new ModelAndView("/departamento-inserir");
		model.addAttribute("titulo", "Atualizar Departamento");
		
		Optional<Departamento> departamento = this.depService.obterPorId(id);
		if (departamento.isPresent()) {
			mv.addObject("departamento", departamento);
		} else {
			mv.setViewName("redirect:/departamentos/");
		}
		
		return mv;
	}
	
	@PostMapping("/editar/{id}")
	public ModelAndView atualizarDepartamento(@Valid Departamento departamento, BindingResult br, @PathVariable Integer id, Model model) {
		ModelAndView mv = new ModelAndView("/departamento-inserir");
		model.addAttribute("titulo", "Atualizar Departamento");
		
		try {
			// só passa se as informações recebidas forem diferentes das anteriores (que estão no banco)
			Optional<Departamento> departamentoExistente = this.depService.obterPorId(departamento.getId());
			if (departamentoExistente.isPresent() && !departamentoExistente.get().equals(departamento)) {
				if (!departamento.getNome().equalsIgnoreCase(departamentoExistente.get().getNome())) {
					if (this.depService.existe(departamento)) {
						br.addError(new ObjectError("departamentoExistente", "Já existe departamento com o nome informado"));
					}
				}
				if (br.hasErrors()) {
					return mv;
				}
				
				this.depService.salvar(departamento);
				mv.addObject("msgSucesso", "Departamento atualizado com sucesso!");
			}
		} catch (Exception e) {
			mv.setViewName("redirect:/departamentos/editar/"+id);
			return mv;
		}
		
		return mv;
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirDepartamento(@PathVariable Integer id, RedirectAttributes ra) {
		try {
			this.depService.deletarPorId(id);
		} catch (Exception e) {
			ra.addFlashAttribute("msgErro", e.getMessage());
		}
		return "redirect:/departamentos/";
	}
	
}
