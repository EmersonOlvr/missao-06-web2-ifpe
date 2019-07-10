package br.ifpe.web2.missoes.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ifpe.web2.missoes.model.Funcionario;
import br.ifpe.web2.missoes.service.FuncionarioService;

@Controller
public class LoginController {

	@Autowired
	private FuncionarioService funcionarioService;

	@GetMapping(value = {"/", "/login"})
	public String viewLogin() {
		return "/login";
	}

	@PostMapping("/login")
	public String login(@RequestParam(required = false) String matricula, @RequestParam(required = false) String senha,
			HttpSession session, Model model) 
	{
		if (Strings.isBlank(matricula)) {
			model.addAttribute("msgErro", "Informe a matrícula");
			return "/login";
		} else if (Strings.isBlank(senha)) {
			model.addAttribute("msgErro", "Informe a senha");
			return "/login";
		}

		Optional<Funcionario> funcionarioExistente = this.funcionarioService.obterPorMatricula(matricula);

		if (funcionarioExistente.isPresent()) {
			if (BCrypt.checkpw(senha, funcionarioExistente.get().getSenha())) {
				session.setAttribute("funcionarioLogado", funcionarioExistente.get());
				return "redirect:/home";
			}
		}

		model.addAttribute("msgErro", "Matrícula e/ou senha incorreta(s).");
		return "/login";
	}

}
