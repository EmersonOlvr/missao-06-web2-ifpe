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
import br.ifpe.web2.missoes.service.CargoService;

@Controller
@RequestMapping("/cargos")
public class CargoController {

	@Autowired private CargoService cargoService;
	
	@GetMapping("/")
	public ModelAndView viewListarCargos(@RequestParam(required = false) String q) {
		ModelAndView mv = new ModelAndView("/cargo-listar");
		if (q != null && !q.isEmpty()) {
			List<Cargo> cargos = this.cargoService.listarTodosPorDescricao(q);
			if (cargos.size() == 1) {
				mv.setViewName("redirect:/cargos/"+cargos.get(0).getId());
			} else {
				mv.addObject("cargos", cargos);
			}
			return mv;
		}
		
		mv.addObject("cargos", this.cargoService.listarPrimeiros10OrdenadosPorDescricao());
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
		
		this.cargoService.salvar(cargo);
		mv.addObject("msgSucesso", "Cargo salvo com sucesso!");
		mv.addObject("cargo", new Cargo());
		
		return mv;
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView viewAtualizarCargo(Model model, @PathVariable Integer id) {
		ModelAndView mv = new ModelAndView("/cargo-inserir");
		model.addAttribute("titulo", "Atualizar Cargo");
		
		Optional<Cargo> cargo = this.cargoService.obterPorId(id);
		if (cargo.isPresent()) {
			mv.addObject("cargo", cargo);
		} else {
			mv.setViewName("redirect:/cargos/");
		}
		
		return mv;
	}
	
	@PostMapping("/editar/{id}")
	public ModelAndView atualizarCargo(@Valid Cargo cargo, BindingResult br, @PathVariable Integer id, Model model) {
		ModelAndView mv = new ModelAndView("/cargo-inserir");
		model.addAttribute("titulo", "Atualizar Cargo");
		
		try {
			// só passa se as informações recebidas forem diferentes das anteriores (que estão no banco)
			Optional<Cargo> cargoExistente = this.cargoService.obterPorId(cargo.getId());
			if (cargoExistente.isPresent() && !cargoExistente.get().equals(cargo)) {
				if (!cargo.getDescricao().equalsIgnoreCase(cargoExistente.get().getDescricao())) {
					if (this.cargoService.existe(cargo)) {
						br.addError(new ObjectError("cargoExistente", "Já existe cargo com a descrição informada"));
					}
				}
				if (br.hasErrors()) {
					return mv;
				}
				
				this.cargoService.salvar(cargo);
				mv.addObject("msgSucesso", "Cargo atualizado com sucesso!");
			}
		} catch (Exception e) {
			mv.setViewName("redirect:/cargos/"+id);
			return mv;
		}
		
		return mv;
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirCargo(@PathVariable Integer id, RedirectAttributes ra) {
		try {
			this.cargoService.deletarPorId(id);
		} catch (Exception e) {
			ra.addFlashAttribute("msgErro", e.getMessage());
		}
		return "redirect:/cargos/";
	}
	
}
