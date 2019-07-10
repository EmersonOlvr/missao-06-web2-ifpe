package br.ifpe.web2.missoes.controller;

import java.util.ArrayList;

import javax.validation.Valid;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ifpe.web2.missoes.exceptions.FotoInvalidaException;
import br.ifpe.web2.missoes.exceptions.FuncionarioNotFoundException;
import br.ifpe.web2.missoes.model.Foto;
import br.ifpe.web2.missoes.model.Funcionario;
import br.ifpe.web2.missoes.service.CargoService;
import br.ifpe.web2.missoes.service.EmpresaService;
import br.ifpe.web2.missoes.service.FotoStorageService;
import br.ifpe.web2.missoes.service.FuncionarioService;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired private FuncionarioService funcService;
	@Autowired private EmpresaService empresaService;
	@Autowired private CargoService cargoService;
	@Autowired private FotoStorageService fotoService;

	@GetMapping("/")
	public ModelAndView viewListarFuncionarios(@RequestParam(required = false) String q) {
		ModelAndView mv = new ModelAndView("/funcionario-listar");
		mv.addObject("funcionarios", this.funcService.listarPrimeiros10OrdenadosPorDataAdmissao());
		return mv;
	}
	
	@GetMapping("/filtrar")
	public ModelAndView viewFiltrarFuncionarios() {
		ModelAndView mv = new ModelAndView("/funcionario-filtrar");
		mv.addObject("cargos", this.cargoService.listarTodosAtivos());
		mv.addObject("empresas", this.empresaService.listarTodasAtivas());
		mv.addObject("funcionario", new Funcionario());
		return mv;
	}
	
	@PostMapping("/filtrar")
	public ModelAndView filtrarFuncionarios(@ModelAttribute Funcionario funcionario, Model model) {
		ModelAndView mv = new ModelAndView("/funcionario-listar");
		mv.addObject("cargos", this.cargoService.listarTodosAtivos());
		mv.addObject("empresas", this.empresaService.listarTodasAtivas());
		
		if (Strings.isBlank(funcionario.getNome()) && Strings.isBlank(funcionario.getCpf()) 
				&& funcionario.getCargo() == null && funcionario.getEmpresa() == null 
				&& (funcionario.getEndereco() == null || Strings.isBlank(funcionario.getEndereco().getCidade())) 
				&& Strings.isBlank(funcionario.getDepartamento())) 
		{
			model.addAttribute("msgErro", "Informe no mínimo um campo para filtrar.");
			mv.setViewName("/funcionario-filtrar");
			return mv;
		}
		
		if (Strings.isBlank(funcionario.getCpf())) {
			funcionario.setCpf(null);
		}
		if (Strings.isBlank(funcionario.getNome())) {
			funcionario.setNome(null);
		}
		if (Strings.isBlank(funcionario.getDepartamento())) {
			funcionario.setDepartamento(null);
		}
		if (Strings.isBlank(funcionario.getEndereco().getCidade())) {
			funcionario.setEndereco(null);
		}
		Example<Funcionario> exemploFuncionario = Example.of(funcionario);
		mv.addObject("funcionarios", this.funcService.findAll(exemploFuncionario));
		
		return mv;
	}
	
	@GetMapping("/inserir")
	public ModelAndView viewInserirFuncionario(Model model) {
		ModelAndView mv = new ModelAndView("/funcionario-inserir");
		mv.addObject("funcionario", new Funcionario());
		mv.addObject("cargos", this.cargoService.listarTodosAtivos());
		mv.addObject("empresas", this.empresaService.listarTodasAtivas());
		model.addAttribute("titulo", "Inserir Funcionário");
		return mv;
	}
	
	@PostMapping("/inserir")
	public ModelAndView inserirFuncionario(@Valid Funcionario funcionario, 
			BindingResult br, @RequestParam MultipartFile file, Model model) 
	{
		ModelAndView mv = new ModelAndView("/funcionario-inserir");
		mv.addObject("cargos", this.cargoService.listarTodosAtivos());
		mv.addObject("empresas", this.empresaService.listarTodasAtivas());
		model.addAttribute("titulo", "Inserir Funcionário");
		
		ArrayList<String> erros = this.funcService.validar(funcionario);
		if (file == null || file.isEmpty()) {
			br.addError(new ObjectError("erroFoto", "Envie uma Foto"));
		} else {
			try {
				Foto foto = this.fotoService.validar(file);
				funcionario.setFoto(foto);
			} catch (FotoInvalidaException e) {
				System.out.println(e.getMessage());
				erros.add(e.getMessage());
			}
		}
		if (erros.size() > 0) {
			erros.forEach(e -> {br.addError(new ObjectError("erroValidacao", e));});
		}
		if (br.hasErrors()) {
			return mv;
		}
		
		this.funcService.salvar(funcionario);
		mv.addObject("msgSucesso", "Funcionário salvo com sucesso!");
		mv.addObject("funcionario", new Funcionario());
		
		return mv;
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView viewAtualizarFuncionario(Model model, @PathVariable Integer id) {
		ModelAndView mv = new ModelAndView("/funcionario-inserir");
		mv.addObject("cargos", this.cargoService.listarTodosAtivos());
		mv.addObject("empresas", this.empresaService.listarTodasAtivas());
		model.addAttribute("titulo", "Atualizar Funcionário");
		model.addAttribute("mostrarInputsOmitidos", true);
		
		try {
			mv.addObject("funcionario", this.funcService.obterPorId(id));
		} catch (FuncionarioNotFoundException e) {
			mv.setViewName("redirect:/funcionarios/");
		}
		
		return mv;
	}
	
	@PostMapping("/editar/{id}")
	public ModelAndView atualizarFuncionario(@Valid Funcionario funcionario, BindingResult br, 
			@RequestParam MultipartFile file, @PathVariable Integer id, Model model) 
	{
		ModelAndView mv = new ModelAndView("/funcionario-inserir");
		mv.addObject("cargos", this.cargoService.listarTodosAtivos());
		mv.addObject("empresas", this.empresaService.listarTodasAtivas());
		model.addAttribute("titulo", "Atualizar Funcionário");
		model.addAttribute("mostrarInputsOmitidos", true);
		
		try {
			funcionario.setFoto(this.fotoService.obterPorFuncionario(funcionario));
		} catch (Exception e) {}
		
		ArrayList<String> erros = this.funcService.validar(funcionario);
		if (file != null && !file.isEmpty()) {
			try {
				Foto foto = this.fotoService.validar(file);
				foto.setId(funcionario.getFoto().getId());
				foto.setFuncionario(funcionario);
				
				funcionario.setFoto(foto);
			} catch (FotoInvalidaException e) {
				erros.add(e.getMessage());
			}
		}
		if (erros.size() > 0) {
			erros.forEach(e -> {br.addError(new ObjectError("erroValidacao", e));});
		}
		if (br.hasErrors()) {
			return mv;
		}
		
		this.funcService.salvar(funcionario);
		mv.addObject("msgSucesso", "Funcionário atualizado com sucesso!");
		
		return mv;
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirFuncionario(@PathVariable Integer id, RedirectAttributes ra) {
		try {
			this.funcService.deletarPorId(id);
		} catch (Exception e) {}
		
		return "redirect:/funcionarios/";
	}
	
}
