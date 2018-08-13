package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import ac.modelo.Certificado;
import ac.modelo.Movimentacao;
import base.modelo.Aluno; 
import dao.GenericDAO;

public class ValidaPeriodoIncricao implements Serializable{

	private List<Movimentacao> movimentacoes;
	
	@PostConstruct
	public void inicializa(){
		movimentacoes = new ArrayList<>();
	}

	private static final long serialVersionUID = 1L;
	
	@Inject
	private GenericDAO<Movimentacao> daoMovimentacao;

	private void recuperarMovimentacao(AlunoTurma aluno) {
		movimentacoes = new ArrayList<>();
		System.out.println("entro" + aluno.getId() ); 
		 
		try {
			movimentacoes = daoMovimentacao.listar(Movimentacao.class, " situacao = 1 and alunoTurma = " + aluno.getId());
			
			System.out.println("tamanhpo " +movimentacoes.size());
			
		} catch (Exception e) {
			System.err.println("Erro em recuperarMovimentacao");
			e.printStackTrace();
		}
	}

	public Boolean permitirCadastroCertificado(Certificado certificado) {
		movimentacoes = new ArrayList<>();
		recuperarMovimentacao(certificado.getAlunoTurma());
		Boolean retorno = true;
		try {
			for (int i = 0; i <= movimentacoes.size() - 1; i++) {
				if ((i == movimentacoes.size() - 1)) {
					if (((certificado.getDataInicio().after(movimentacoes.get(i).getDataMovimentacao()))
							|| (certificado.getDataInicio().equals(movimentacoes.get(i).getDataMovimentacao())))) {
						retorno = false;
						break;
					}
				} else {
					if (((certificado.getDataInicio().after(movimentacoes.get(i).getDataMovimentacao()))
							|| (certificado.getDataInicio().equals(movimentacoes.get(i).getDataMovimentacao())))
							&& ((certificado.getDataFinalizado().before(movimentacoes.get(i).getDataMovimentacaoFim()))
									|| (certificado.getDataFinalizado()
											.equals(movimentacoes.get(i).getDataMovimentacaoFim())))) {
						retorno = false;
						break;
					}

				}
			}
		} catch (Exception e) {
			System.err.println("Erro em permitirCadastroCertificado ");
			e.printStackTrace();
		}
		return retorno;
	}

}
