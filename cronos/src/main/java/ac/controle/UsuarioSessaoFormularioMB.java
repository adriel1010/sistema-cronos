package ac.controle;

import ac.modelo.Pessoa;


import base.modelo.Aluno;
import base.modelo.Servidor;
import dao.UsuarioDAO;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@SessionScoped
@Named("UsuarioSessaoFormularioMB")
public class UsuarioSessaoFormularioMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Pessoa pessoa;
	private Servidor servidor;
	private Aluno usuarioAluno;
	private Servidor secretaria;
	private Servidor administrador;


	@Inject
	private UsuarioDAO daoUsuario;

	
	 public void recuperaLogado(Pessoa pessoaLogada) {
		
		 System.out.println("chamo aqio :: "+pessoaLogada.getUsuario());
		 
		 
		pessoa = pessoaLogada;
		SecurityContext context = SecurityContextHolder.getContext();
		if (context instanceof SecurityContext) {
			Authentication authentication = context.getAuthentication();
			if (authentication instanceof Authentication) {
				pessoa.setUsuario(((User) authentication.getPrincipal()).getUsername());
				
				
				
			}
		}
		System.out.println("pessoa logada "+pessoa.getNome());
	}

	public Pessoa recuperarAluno() {
		usuarioAluno = new Aluno();
		
		try {
			usuarioAluno = (Aluno) daoUsuario.retornarLogado(Aluno.class, pessoa.getUsuario()).get(0);
			
		} catch (Exception e) {
		}
		return usuarioAluno;
	}
	
	public Pessoa recuperarServidor() {
		servidor = new Servidor();
	
		try {
			servidor = (Servidor) daoUsuario.retornarLogado(Servidor.class, pessoa.getUsuario()).get(0);
		} catch (Exception e) {
		}
		
		
		
		return servidor;
	}	
	
	
	
	public Servidor recuperarCadastroServidor() {
	
		servidor = new Servidor();
	
		try {
			servidor = (Servidor) daoUsuario.retornarLogado(Servidor.class, pessoa.getUsuario()).get(0);
		} catch (Exception e) {
		}
		return servidor;
	}
	
	

	public Servidor recuperarSecretaria() {

		
		secretaria = new Servidor();
		
		try {
			secretaria = (Servidor) daoUsuario.retornarLogado(Servidor.class, pessoa.getUsuario()).get(0);
			
		} catch (Exception e) {
		}
		return secretaria;
		
	}

	public Servidor recuperarProfessor() { //public Professor recuperarProfessor() {
		
		servidor = new Servidor();// professor = new Professor();
		
		try {
			servidor = (Servidor) daoUsuario.retornarLogado(Servidor.class, pessoa.getUsuario()).get(0);
		} catch (Exception e) {
		}
		return servidor;
	}

	public Servidor recuperarAdmisnitrador() {


		administrador = new Servidor();
	
		try {
			administrador = (Servidor) daoUsuario.retornarLogado(Servidor.class, pessoa.getUsuario()).get(0);
		} catch (Exception e) {
		}
		return servidor;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
