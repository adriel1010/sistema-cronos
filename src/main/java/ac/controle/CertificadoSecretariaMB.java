package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import ac.modelo.AlunoTurma;
import ac.modelo.Certificado;
import base.modelo.Servidor;
import base.service.CertificadoService; 
import dao.GenericDAO;

@ViewScoped
@Named("certificadoSecretariaMB")
public class CertificadoSecretariaMB implements Serializable{
	
	
	/* 
	 *
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Certificado certificado;

	private List<Certificado> certificadosPendentes;
	private List<Certificado> certificadosInvalidos;
	private List<Certificado> certificadosAutenticados;

	@Inject
	private CertificadoService certificadoService;
	
	@Inject
	private GenericDAO<Certificado> daoCertificado; 	
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	
	@PostConstruct
	public void inicializar() {
		certificado = new Certificado();
	
		certificadosPendentes = new ArrayList<>();
		certificadosInvalidos = new ArrayList<>();
		certificadosAutenticados = new ArrayList<>();
		preencherListaPendentes();
		preencherListaAutenticados();
		preencherListaInvalidos();
		
		
	}

	public void autenticar() {
		try {
			certificado.setSituacao(1);
			Servidor secretariaRecuperarSesao = (Servidor) usuarioSessao.recuperarSecretaria();
			certificado.setAutenticadoSecretaria(
					secretariaRecuperarSesao.getNome() + ", " + secretariaRecuperarSesao.getUsuario());

			certificadoService.inserirAlterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCertificadoAutenticarSecretaria();
			preencherListaPendentes();
			preencherListaAutenticados();
			preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	public void invalidar() {
		try {
			certificado.setSituacao(2);
			Servidor secretariaRecuperarSesao = (Servidor) usuarioSessao.recuperarSecretaria();
			certificado.setAutenticadoSecretaria(
					secretariaRecuperarSesao.getNome() + ", " + secretariaRecuperarSesao.getUsuario());
			certificadoService.inserirAlterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCertificadoInvalidarSecretaria();
			preencherListaPendentes();
			preencherListaAutenticados();
			preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	public void pender() {
		try {
			certificado.setSituacao(0);
			Servidor secretariaRecuperarSesao = (Servidor) usuarioSessao.recuperarSecretaria();
			certificado.setAutenticadoSecretaria(
					secretariaRecuperarSesao.getNome() + ", " + secretariaRecuperarSesao.getUsuario());
			certificadoService.inserirAlterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCertificadoPenderSecretaria();
			preencherListaPendentes();
			preencherListaAutenticados();
			preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	private void criarNovoCertificado() {
		certificado = new Certificado();
	}

	private void preencherListaPendentes() {
	
		certificadosPendentes = daoCertificado.listar(Certificado.class, " situacao = 0 ");
	}

	private void preencherListaAutenticados() {
		certificadosAutenticados = daoCertificado.listar(Certificado.class, " situacao = 1 ");
	}

	private void preencherListaInvalidos() {
		certificadosInvalidos = daoCertificado.listar(Certificado.class, " situacao = 2 ");
	}

	public Certificado getCertificado() {
		return certificado;
	}

	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
	}

	public List<Certificado> getCertificadosPendentes() {
		return certificadosPendentes;
	}

	public void setCertificadosPendentes(List<Certificado> certificadosPendentes) {
		this.certificadosPendentes = certificadosPendentes;
	}

	public List<Certificado> getCertificadosInvalidos() {
		return certificadosInvalidos;
	}

	public void setCertificadosInvalidos(List<Certificado> certificadosInvalidos) {
		this.certificadosInvalidos = certificadosInvalidos;
	}

	public List<Certificado> getCertificadosAutenticados() {
		return certificadosAutenticados;
	}

	public void setCertificadosAutenticados(List<Certificado> certificadosAutenticados) {
		this.certificadosAutenticados = certificadosAutenticados;
	}
}
