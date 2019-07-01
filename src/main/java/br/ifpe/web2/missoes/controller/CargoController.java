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

import br.ifpe.web2.missoes.model.Cargo;
import br.ifpe.web2.missoes.model.Funcionario;
import br.ifpe.web2.missoes.service.CargoService;
import br.ifpe.web2.missoes.service.FuncionarioService;

@Controller
@RequestMapping("/cargos")
public class CargoController {

	@Autowired private CargoService cargoService;
	@Autowired private FuncionarioService funcService;
	
	@GetMapping("/")
	public ModelAndView viewListarCargos(@RequestParam(required = false) String q) {
		ModelAndView mv = new ModelAndView("/cargo-listar");
		if (q != null && !q.isEmpty()) {
			List<Cargo> cargos = this.cargoService.findAllByDescricao(q);
			if (cargos.size() == 1) {
				mv.setViewName("redirect:/cargos/"+cargos.get(0).getId());
			} else {
				mv.addObject("cargos", cargos);
			}
			return mv;
		}
		
		mv.addObject("cargos", this.cargoService.findFirst10ByOrderByDescricaoAsc());
		return mv;
	}
	
	@GetMapping("/inserir")
	public ModelAndView viewInserirCargo(Model model) {
		ModelAndView mv = new ModelAndView("/cargo-inserir");
		model.addAttribute("titulo", "Inserir Cargo");
		
		mv.addObject("cargo", new Cargo());
		return mv;
	}
	
	@PostMapping("/inserir")
	public ModelAndView inserirCargo(@Valid Cargo cargo, BindingResult br, Model model) {
		ModelAndView mv = new ModelAndView("/cargo-inserir");
		model.addAttribute("titulo", "Inserir Cargo");
		
		if (this.cargoService.existe(cargo)) {
			br.addError(new ObjectError("cargoExistente", "Já existe cargo com a descrição informada"));
		}
		if (br.hasErrors()) {
			return mv;
		}
		
		this.cargoService.save(cargo);
		mv.addObject("msgSucesso", "Cargo salvo com sucesso!");
		mv.addObject("cargo", new Cargo());
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView viewAtualizarCargo(Model model, @PathVariable Integer id) {
		ModelAndView mv = new ModelAndView("/cargo-inserir");
		model.addAttribute("titulo", "Atualizar Cargo");
		
		Optional<Cargo> cargo = this.cargoService.findById(id);
		if (cargo.isPresent()) {
			mv.addObject("cargo", cargo);
		} else {
			mv.setViewName("redirect:/cargos/");
		}
		
		return mv;
	}
	
	@PostMapping("/{id}")
	public ModelAndView atualizarCargo(@Valid Cargo cargo, BindingResult br, Integer id, Model model) {
		ModelAndView mv = new ModelAndView("/cargo-inserir");
		model.addAttribute("titulo", "Atualizar Cargo");
		
		try {
			// só passa se as informações recebidas forem diferentes das anteriores (que estão no banco)
			Optional<Cargo> cargoExistente = this.cargoService.findById(cargo.getId());
			if (cargoExistente.isPresent() && !cargoExistente.get().equals(cargo)) {
				if (!cargo.getDescricao().equalsIgnoreCase(cargoExistente.get().getDescricao())) {
					if (this.cargoService.existe(cargo)) {
						br.addError(new ObjectError("cargoExistente", "Já existe cargo com a descrição informada"));
					}
				}
				if (br.hasErrors()) {
					return mv;
				}
				
				this.cargoService.save(cargo);
				mv.addObject("msgSucesso", "Cargo atualizado com sucesso!");
			}
		} catch (Exception e) {
			mv.setViewName("redirect:/cargos/"+id);
			return mv;
		}
		
		return mv;
	}
	
	@GetMapping("/excluir/{id}")
	public ModelAndView excluirCargo(@PathVariable Integer id, RedirectAttributes ra) {
		Optional<Cargo> cargo = this.cargoService.findById(id);
		if (cargo.isPresent()) {
			List<Funcionario> funcs = this.funcService.findAllByCargo(cargo.get());
			if (funcs.size() > 0) {
				ra.addFlashAttribute("msgErro", "Não é possível excluir o(s) cargo(s) de funcionário"
						+ " selecionado(s) devido a vínculos com outras informações.");
			} else {
				this.cargoService.deleteById(id);
			}
		}
		return new ModelAndView("redirect:/cargos/");
	}
	
}
