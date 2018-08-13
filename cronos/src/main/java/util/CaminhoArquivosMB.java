package util;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("caminhoArquivosMB")
@RequestScoped
public class CaminhoArquivosMB implements Serializable {

	private static final long serialVersionUID = 1L;
//	private String caminhoUmaPastaCertificado = "../certificadosteste/";
	  private String caminhoUmaPastaCertificado = "../certificados/";
	  
	  
//	private String caminhoDuasPastaCertificado = " ../../certificadosteste/";
	 private String caminhoDuasPastaCertificado = " ../../certificados/";
	 
	 
	 
//	private String caminhoTresPastaCertificado = "../../../certificadosteste/";
	 private String caminhoTresPastaCertificado = "../../../certificados/";

	// private String caminhoUmaPastaCertificado = "../protocolosteste/";
	private String caminhoUmaPastaProtocolo = "../protocolosteste/";
	private String caminhoDuasPastaProtocolo = " ../../protocolosteste/";
	private String caminhoTresPastaProtocolo = "../../../protocolosteste/";

	public String getCaminhoUmaPastaCertificado() {
		return caminhoUmaPastaCertificado;
	}

	public void setCaminhoUmaPastaCertificado(String caminhoUmaPastaCertificado) {
		this.caminhoUmaPastaCertificado = caminhoUmaPastaCertificado;
	}

	public String getCaminhoDuasPastaCertificado() {
		return caminhoDuasPastaCertificado;
	}

	public void setCaminhoDuasPastaCertificado(String caminhoDuasPastaCertificado) {
		this.caminhoDuasPastaCertificado = caminhoDuasPastaCertificado;
	}

	public String getCaminhoTresPastaCertificado() {
		return caminhoTresPastaCertificado;
	}

	public void setCaminhoTresPastaCertificado(String caminhoTresPastaCertificado) {
		this.caminhoTresPastaCertificado = caminhoTresPastaCertificado;
	}

	public String getCaminhoUmaPastaProtocolo() {
		return caminhoUmaPastaProtocolo;
	}

	public void setCaminhoUmaPastaProtocolo(String caminhoUmaPastaProtocolo) {
		this.caminhoUmaPastaProtocolo = caminhoUmaPastaProtocolo;
	}

	public String getCaminhoDuasPastaProtocolo() {
		return caminhoDuasPastaProtocolo;
	}

	public void setCaminhoDuasPastaProtocolo(String caminhoDuasPastaProtocolo) {
		this.caminhoDuasPastaProtocolo = caminhoDuasPastaProtocolo;
	}

	public String getCaminhoTresPastaProtocolo() {
		return caminhoTresPastaProtocolo;
	}

	public void setCaminhoTresPastaProtocolo(String caminhoTresPastaProtocolo) {
		this.caminhoTresPastaProtocolo = caminhoTresPastaProtocolo;
	}

}
