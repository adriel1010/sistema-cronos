package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.GrupoTurma;
import ac.services.AlunoService;
import base.modelo.Aluno;
import base.modelo.Turma;
import dao.FiltrosDAO;
import dao.GenericDAO;

public class VerificaSituacaoTurma implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<GrupoTurma> grupoTurmas;
	private List<Certificado> certificados;
	private List<AlunoTurma> alunosTurmas;
	private List<AtividadeTurma> listAtividadeTurma;
	private List<Double> listHoras;

	@Inject
	private GenericDAO<GrupoTurma> daoGrupoTurma;

	@Inject
	private GenericDAO<Certificado> daoCertificado;

	@Inject
	private AlunoService alunoService;

	@Inject
	private FiltrosDAO daoFiltros;

	public VerificaSituacaoTurma() {
		grupoTurmas = new ArrayList<>();
		alunosTurmas = new ArrayList<>();
		certificados = new ArrayList<>();

	}

	public void recuperarGrupoTurma(Turma turma) {
		grupoTurmas = daoGrupoTurma.listar(GrupoTurma.class, " matriz = " + turma.getMatriz().getId());
	}

	public void recuperarAlunoTurmas(Turma turma) {
		alunosTurmas = daoFiltros.buscarAlunoTurma(turma.getId());

	}

	public void recuperarCertificados(Turma turma) {
		// busca todos os alunos da turma
		recuperarAlunoTurmas(turma);

		// vai passar um por um dos alunos recuperados
		for (AlunoTurma a : alunosTurmas) {
			// aqui busca todos os certificados q foram autenticados / de cada
			// um dos alunos
			certificados.addAll(
					daoCertificado.listar(Certificado.class, " alunoTurma = " + a.getId() + " and situacao = 3"));
			// aqui vai mandar calcular a situação
			calcularSituacao(turma, a);
			certificados = new ArrayList<>();

		}

	}

	public void recuperarCertificadosAluno(AlunoTurma aluno) {

		certificados.clear();
		certificados.addAll(
				daoCertificado.listar(Certificado.class, " alunoTurma = " + aluno.getId() + " and situacao = 3"));
		// aqui vai mandar calcular a situação
		calcularSituacao(aluno.getTurma(), aluno);

	}

	// aqui vem todos os certificados do aluno x "faz isso com todos da turma"
	public void calcularSituacao(Turma turma, AlunoTurma alunoTurma) {
		// aqui manda recuperar todos os grupos dessa turma

		recuperarGrupoTurma(turma);
		List<Boolean> situacoes = new ArrayList<>();
		listHoras = new ArrayList<>();
		listAtividadeTurma = new ArrayList<>();
		Boolean situacaoAux = true;
		Boolean verifica = true;
	
		Double somaTotalHoras = 0.0;
		Double horasComputadas = 0.0;
		Double horasG1 = 0.0;
		Double horasMatriz = 0.0;
		Boolean auxiliaAtividadeTurma = true;

		// aqui ja recuperou os grupos, agora vai passar grupo por grupo
		for (GrupoTurma g : grupoTurmas) {
		
			horasMatriz = g.getMatriz().getTotalHoras();
		}

		for (GrupoTurma g : grupoTurmas) {

			for (Certificado c : certificados) {
				// compara se o id do grupoTurma do certificado é igual ao id do
				// grupoTurma
				// aqui pega todos os certificados do mesmo grupo ->

				if (c.getIdGrupoTurma().equals(g.getId())) {

					// aqui pega as horas desse grupo

					for (AtividadeTurma a : listAtividadeTurma) {
						if (a.getId() == c.getAtividadeTurma().getId()) {
							auxiliaAtividadeTurma = false;
						}
					}
					if (auxiliaAtividadeTurma) {
						listAtividadeTurma.add(c.getAtividadeTurma());
					} else {
						auxiliaAtividadeTurma = true;
					}

				}

			}
			//aqui eu tenho as atividades que a pessoa tem cadastrada, agora esse método pega cada uma dessas atividades e vai buscar
			//nos certificados elas, verificando se ta essas atividade bate a hr maxima por atividade ou não. 
			for (AtividadeTurma ativ : listAtividadeTurma) {
				horasG1 += buscarHoras(ativ);
				horasComputadas += buscarHorasTotais(ativ);
			}
			somaTotalHoras += horasG1;

			listHoras.add(horasG1);
			listHoras.add(horasComputadas);

			 
			if (horasG1 < g.getMinimoHoras()) {
				verifica = false;
			}
			horasG1 = 0.;
			horasComputadas = 0.;
			listAtividadeTurma = new ArrayList<>();
		}

		alterarHoras(alunoTurma);

		if (verifica) {
			if (verificaSituacao(somaTotalHoras, horasMatriz)) {
				alterarSituacao(alunoTurma, true);
			} else {
				alterarSituacao(alunoTurma, false);
			}

		} else {
			alterarSituacao(alunoTurma, false);
		}
		somaTotalHoras = 0.0;
		verifica = true;
		listHoras = new ArrayList<>();

	}

	public void alterarHoras(AlunoTurma aluno) {

		for (int i = 0; i < 1; i++) {
			alunoService.update(" Aluno set totalHorasCadastradasG1 = '" + listHoras.get(1)
					+ "' , totalHorasComputadasG1 = '" + listHoras.get(0) + "' , totalHorasCadastradasG2 = '"
					+ listHoras.get(3) + "' , totalHorasComputadasG2 = '" + listHoras.get(2)
					+ "' , totalHorasCadastradasG3 = '" + listHoras.get(5) + "' , totalHorasComputadasG3 = '"
					+ listHoras.get(4) + "'  where id = " + aluno.getAluno().getId());
		}

	}

	public Boolean verificaSituacao(Double horasTotais, Double horasMatriz) {

		if (horasTotais >= horasMatriz) {
			return true;
		} else {
			return false;
		}

	}

	public Double buscarHoras(AtividadeTurma atividade) {
		Double horasComputadas = 0.;

		for (Certificado c : certificados) {
			if (atividade.getId() == c.getAtividadeTurma().getId()) {
				horasComputadas += c.getHoraComputada();
			}
		}
		if (horasComputadas > atividade.getMaximoHoras()) {
			return atividade.getMaximoHoras();
		} else {
			return horasComputadas;
		}
	}

	public Double buscarHorasTotais(AtividadeTurma atividade) {
		Double horasComputadas = 0.;
		for (Certificado c : certificados) {
			if (atividade.getId() == c.getAtividadeTurma().getId()) {
				horasComputadas += c.getHoraComputada();
			}
		}

		return horasComputadas;

	}

	public void alterarSituacao(AlunoTurma alunoTurma, Boolean situacao) {
		if (situacao) {
			alunoService.update(" Aluno set situacao = 'Completou' where id = " + alunoTurma.getAluno().getId());
		} else {
			alunoService.update(" Aluno set situacao = 'Não Completou' where id = " + alunoTurma.getAluno().getId());
		}
	}
}
