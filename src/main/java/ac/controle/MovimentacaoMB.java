package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.RecuperarRelacoesProfessor;
import ac.modelo.AlunoTurma;
import ac.modelo.Movimentacao;
import base.modelo.Aluno;

import dao.GenericDAO;

@SessionScoped
@Named("movimentacaoMB")
public class MovimentacaoMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Movimentacao> movimentacaosSecretaria;
	private List<Movimentacao> movimentacaosAluno;
	private List<Movimentacao> movimentacaosProfessor;
	private Aluno aluno;
	
	@Inject
	private GenericDAO<Movimentacao> daoMovimentacao; 
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	@Inject
	private RecuperarRelacoesProfessor recuperarRelacoesProfessor;

	@PostConstruct
	public void inicializar() {
		
		movimentacaosSecretaria = new ArrayList<>();
		movimentacaosAluno = new ArrayList<>();
		movimentacaosProfessor = new ArrayList<>();

	}

	public void preencherMovimentacaoSecretaria() {
		movimentacaosSecretaria = daoMovimentacao.listaComStatus(Movimentacao.class);
	}

	public void preencherMovimentacaoAluno() {
		aluno = new Aluno();
		aluno = (Aluno) usuarioSessao.recuperarAluno();
		movimentacaosAluno = daoMovimentacao.listar(Movimentacao.class, " alunoTurma.aluno = " + aluno.getId());
	
	}

	public void preencherMovimentacaoProfessor() {		
		
		movimentacaosProfessor = new ArrayList<>();
		movimentacaosProfessor = recuperarRelacoesProfessor.recuperarTodasMovimentacoesAtivas();
		
		
	}

	public List<Movimentacao> getMovimentacaosSecretaria() {
		return movimentacaosSecretaria;
	}

	public void setMovimentacaosSecretaria(List<Movimentacao> movimentacaosSecretaria) {
		this.movimentacaosSecretaria = movimentacaosSecretaria;
	}

	public List<Movimentacao> getMovimentacaosAluno() {
		
		return movimentacaosAluno;
	}

	public void setMovimentacaosAluno(List<Movimentacao> movimentacaosAluno) {
		this.movimentacaosAluno = movimentacaosAluno;
	}

	public List<Movimentacao> getMovimentacaosProfessor() {
		return movimentacaosProfessor;
	}

	public void setMovimentacaosProfessor(List<Movimentacao> movimentacaosProfessor) {
		this.movimentacaosProfessor = movimentacaosProfessor;
	}

}
