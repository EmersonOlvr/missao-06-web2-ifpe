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

import br.ifpe.web2.missoes.model.Empresa;
import br.ifpe.web2.missoes.service.EmpresaService;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {

	@Autowired private EmpresaService empresaService;
	
	@GetMapping("/")
	public ModelAndView viewListarEmpresas(@RequestParam(required = false) String q) {
		ModelAndView mv = new ModelAndView("/empresa-listar");
		if (q != null && !q.isEmpty()) {
			List<Empresa> empresas = this.empresaService.listarTodasPorNome(q);
			if (empresas.size() == 1) {
				mv.setViewName("redirect:/empresas/"+empresas.get(0).getId());
			} else {
				mv.addObject("empresas", empresas);
			}
			return mv;
		}
		
		mv.addObject("empresas", this.empresaService.listarPrimeiras10OrdenadasPorNome());
		return mv;
	}
	
	@GetMapping("/inserir")
	public ModelAndView viewInserirEmpresa(Model model) {
		ModelAndView mv = new ModelAndView("/empresa-inserir");
		model.addAttribute("titulo", "Inserir Empresa");
		
		mv.addObject("empresa", new Empresa());
		return mv;
	}
	
	@PostMapping("/inserir")
	public ModelAndView inserirEmpresa(@Valid Empresa empresa, BindingResult br, Model model) {
		ModelAndView mv = new ModelAndView("/empresa-inserir");
		model.addAttribute("titulo", "Inserir Empresa");
		
		if (this.empresaService.existe(empresa)) {
			br.addError(new ObjectError("empresaNomeExistente", "Já existe empresa com o nome informado"));
		}
		if (empresa.getPrincipal() && this.empresaService.existePrincipal()) {
			br.addError(new ObjectError("empresaPrincipalExistente", "Já existe empresa principal"));
		}
		if (br.hasErrors()) {
			return mv;
		}
		
		this.empresaService.salvar(empresa);
		mv.addObject("msgSucesso", "Empresa salva com sucesso!");
		mv.addObject("empresa", new Empresa());
		
		return mv;
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView viewAtualizarEmpresa(Model model, @PathVariable Integer id) {
		ModelAndView mv = new ModelAndView("/empresa-inserir");
		model.addAttribute("titulo", "Atualizar Empresa");
		
		Optional<Empresa> empresa = this.empresaService.obterPorId(id);
		if (empresa.isPresent()) {
			mv.addObject("empresa", empresa);
		} else {
			mv.setViewName("redirect:/empresas/");
		}
		
		return mv;
	}
	
	@PostMapping("/editar/{id}")
	public ModelAndView atualizarEmpresa(@Valid Empresa empresa, BindingResult br, @PathVariable Integer id, Model model) {
		ModelAndView mv = new ModelAndView("/empresa-inserir");
		model.addAttribute("titulo", "Atualizar Empresa");
		
		try {
			// só passa se as informações recebidas forem diferentes das anteriores (que estão no banco)
			Optional<Empresa> empresaExistente = this.empresaService.obterPorId(empresa.getId());
			if (empresaExistente.isPresent() && !empresaExistente.get().equals(empresa)) {
				if (!empresa.getNome().equalsIgnoreCase(empresaExistente.get().getNome())) {
					if (this.empresaService.existe(empresa)) {
						br.addError(new ObjectError("empresaExistente", "Já existe empresa com o nome informado"));
					}
				}
				if (br.hasErrors()) {
					return mv;
				}
				
				this.empresaService.salvar(empresa);
				mv.addObject("msgSucesso", "Empresa atualizado com sucesso!");
			}
		} catch (Exception e) {
			mv.setViewName("redirect:/empresas/"+id);
			return mv;
		}
		
		return mv;
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirEmpresa(@PathVariable Integer id, RedirectAttributes ra) {
		try {
			this.empresaService.deletarPorId(id);
		} catch (Exception e) {
			ra.addFlashAttribute("msgErro", e.getMessage());
		}
		return "redirect:/empresas/";
	}
	
}
