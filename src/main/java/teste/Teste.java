package teste;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.EntityManager;

import ac.modelo.AlunoTurma;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import base.modelo.Aluno;
import base.modelo.Curso; 
import dao.GenericDAO;


//@SessionScoped
//@Named("Teste")
public class Teste{

//	
//	@Inject
//	private GenericDAO<Curso> dao;
//	
//	private Curso curso;
//	
//	@PostConstruct
//	public void iniciar(){
//		 curso = new Curso();	
//	}

	
	public static void main(String args []){ 
//		EntityManager entityManager;
//			
//		entityManager =  ConexaoBanco.getConexao().getEm();
		

//		Teste t = new Teste();
//		t.busca();
//     
	}
	
//	public void busca(){
//		
//		
//		try{
//		curso =  dao.buscarPorId(Curso.class, 3L);
//	     System.out.println("curso "+curso.getNome());
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
}
