package cope.service;

import java.io.Serializable;

import javax.inject.Inject;
 

import ac.modelo.AlunoTurma;
import base.modelo.Tipo;
import cope.modelo.Avaliacao;
import cope.modelo.Projeto;
import dao.GenericDAO;
import questionario.modelo.Email;
import questionario.modelo.Formulario;
import util.Transacional;

public class AvaliacaoService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Avaliacao> dao;
	
	@Transacional
	public void inserirAlterar(Avaliacao obj){
	
		if(obj.getId()==null){
			dao.inserir(obj);
		}else{
			dao.alterar(obj);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		dao.update(valor);
	}

}
