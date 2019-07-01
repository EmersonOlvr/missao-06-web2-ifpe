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
import br.ifpe.web2.missoes.model.Funcionario;
import br.ifpe.web2.missoes.service.EmpresaService;
import br.ifpe.web2.missoes.service.FuncionarioService;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {

	@Autowired private EmpresaService empresaService;
	@Autowired private FuncionarioService funcService;
	
	@GetMapping("/")
	public ModelAndView viewListarEmpresas(@RequestParam(required = false) String q) {
		ModelAndView mv = new ModelAndView("/empresa-listar");
		if (q != null && !q.isEmpty()) {
			List<Empresa> empresas = this.empresaService.findAllByNome(q);
			if (empresas.size() == 1) {
				mv.setViewName("redirect:/empresas/"+empresas.get(0).getId());
			} else {
				mv.addObject("empresas", empresas);
			}
			return mv;
		}
		
		mv.addObject("empresas", this.empresaService.findFirst10ByOrderByNomeAsc());
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
			br.addError(new ObjectError("empresaExistente", "Já existe empresa com o nome informado"));
		}
		if (br.hasErrors()) {
			return mv;
		}
		
		this.empresaService.save(empresa);
		mv.addObject("msgSucesso", "Empresa salva com sucesso!");
		mv.addObject("empresa", new Empresa());
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView viewAtualizarEmpresa(Model model, @PathVariable Integer id) {
		ModelAndView mv = new ModelAndView("/empresa-inserir");
		model.addAttribute("titulo", "Atualizar Empresa");
		
		Optional<Empresa> empresa = this.empresaService.findById(id);
		if (empresa.isPresent()) {
			mv.addObject("empresa", empresa);
		} else {
			mv.setViewName("redirect:/empresas/");
		}
		
		return mv;
	}
	
	@PostMapping("/{id}")
	public ModelAndView atualizarEmpresa(@Valid Empresa empresa, BindingResult br, Integer id, Model model) {
		ModelAndView mv = new ModelAndView("/empresa-inserir");
		model.addAttribute("titulo", "Atualizar Empresa");
		
		try {
			// só passa se as informações recebidas forem diferentes das anteriores (que estão no banco)
			Optional<Empresa> empresaExistente = this.empresaService.findById(empresa.getId());
			if (empresaExistente.isPresent() && !empresaExistente.get().equals(empresa)) {
				if (!empresa.getNome().equalsIgnoreCase(empresaExistente.get().getNome())) {
					if (this.empresaService.existe(empresa)) {
						br.addError(new ObjectError("empresaExistente", "Já existe empresa com o nome informado"));
					}
				}
				if (br.hasErrors()) {
					return mv;
				}
				
				this.empresaService.save(empresa);
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
		Optional<Empresa> empresa = this.empresaService.findById(id);
		if (empresa.isPresent()) {
			List<Funcionario> funcs = this.funcService.findAllByEmpresa(empresa.get());
			if (funcs.size() > 0) {
				ra.addFlashAttribute("msgErro", "Não é possível excluir a(s) empresa(s) "
						+ "selecionada(s) devido a vínculos com outras informações.");
			} else {
				this.empresaService.deleteById(id);
			}
		}
		return "redirect:/empresas/";
	}
	
}
