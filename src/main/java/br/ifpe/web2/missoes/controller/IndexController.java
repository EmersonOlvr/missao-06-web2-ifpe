package br.ifpe.web2.missoes.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/home")
	public String viewHome() {
		return "/home";
	}
	
	@GetMapping("/acesso-negado")
	public String viewAcessoNegado() {
		return "/acesso-negado";
	}
	
	@GetMapping("/sair")
	public String sair(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
}
