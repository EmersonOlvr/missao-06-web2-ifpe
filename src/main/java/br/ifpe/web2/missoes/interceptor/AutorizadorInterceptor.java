package br.ifpe.web2.missoes.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import br.ifpe.web2.missoes.model.Funcionario;

public class AutorizadorInterceptor implements HandlerInterceptor {

	private static final boolean CONTROLAR_ACESSO = true;
	
	private static final String PAGINA_HOME = "/home";
	private static final String PAGINA_ACESSO_NEGADO = "/acesso-negado";
	
	private static final String[] DEP_AUTORIZADOS = {"Gestão de Pessoas"};
	
	private static final String[] PAGINAS_DESLOGADO = {"/", "/login"};
	private static final String[] PAGINAS_LOGADO = {PAGINA_HOME, PAGINA_ACESSO_NEGADO, "/sair", 
													"/funcionarios/", "/cargos/", "/empresas/", "/departamentos/"};
	private static final String[] PAGINAS_PRIVADAS = {"/funcionarios/inserir", "/funcionarios/editar/", "/funcionarios/excluir/"};
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String contextPath = request.getServletPath().toString();
		Funcionario funcLogado = (Funcionario) request.getSession().getAttribute("funcionarioLogado");
		boolean funcEstaLogado = funcLogado != null ? true : false;
		
		//System.out.println("URL Requisitada: "+contextPath);
		//System.out.println("Logado: "+funcEstaLogado);
		
		if (!CONTROLAR_ACESSO) {
			return true;
		}
		
		for (String paginaPrivada : PAGINAS_PRIVADAS) {
			if (contextPath.contains(paginaPrivada)) {
				//System.out.println("paginaPrivada: "+contextPath);
				if (funcEstaLogado) {
					for (String depAutorizado : DEP_AUTORIZADOS) {
						if (funcLogado.getDepartamento().getNome().equalsIgnoreCase(depAutorizado)) {
							return true;
						}
					}
					request.getRequestDispatcher(PAGINA_ACESSO_NEGADO).forward(request, response);
					return false;
				} else {
					response.sendRedirect("/login?destino="+contextPath);
					return false;
				}
			}
		}
		for (String paginaLogado : PAGINAS_LOGADO) {
			if (contextPath.contains(paginaLogado)) {
				//System.out.println("paginaLogado: "+contextPath);
				if (funcEstaLogado) {
					return true;
				} else {
					response.sendRedirect("/login?destino="+contextPath);
					return false;
				}
			}
		}
		for (String paginaDeslogado : PAGINAS_DESLOGADO) {
			if (contextPath.equals(paginaDeslogado)) {
				//System.out.println("paginaDeslogado");
				if (!funcEstaLogado) {
					return true;
				} else {
					response.sendRedirect(PAGINA_HOME);
					return false;
				}
			}
		}
		
		return true;
	}
	
}
