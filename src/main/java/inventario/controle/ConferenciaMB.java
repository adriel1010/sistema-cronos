package inventario.controle;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.loader.custom.Return;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.PermiteInativar;
import ac.controle.UsuarioSessaoMB;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.Grupo;
import ac.modelo.Permissao;
import ac.services.GrupoService;
import base.modelo.Servidor;
import dao.GenericDAO;
import inventario.modelo.EquipamentoInventario;
import inventario.modelo.EstadoConservacao;
import inventario.modelo.Inventario;
import inventario.modelo.Localidades;
import inventario.modelo.LocalInventario;
import inventario.modelo.Tombamento;
import inventario.service.EquipamentoInventarioService;
import inventario.service.LocalService;

@ViewScoped
@Named("conferenciaMB")
public class ConferenciaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private LocalInventario localInventarioSelecionado;
	private List<Localidades> listLocal;
	private EquipamentoInventario equipamentoInventario;
	private Tombamento tombamento;
	private List<Tombamento> listTombamento;
	private List<EquipamentoInventario> listEquipamentoInventario;
	private Inventario inventario;
	private Servidor servidor;
	private boolean chefe = false;

	@Inject
	private GenericDAO<Servidor> daoServidor;

	@Inject
	private GenericDAO<Tombamento> daoTombamento;

	@Inject
	private GenericDAO<EquipamentoInventario> daoEquipamentoInventario;

	@Inject
	private GenericDAO<Inventario> daoInventario;

	@Inject
	private GenericDAO<Permissao> daoPermissao;

	@Inject
	private GenericDAO<Localidades> daoLocal;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private GenericDAO<LocalInventario> daoLocalInventario;

	@Inject
	private LocalService localService;

	@Inject
	private EquipamentoInventarioService equipamentoInventarioService;

	@PostConstruct
	public void inicializar() {
		localInventarioSelecionado = new LocalInventario();
		inventario = new Inventario();
		listLocal = new ArrayList<>();
		equipamentoInventario = new EquipamentoInventario();
		tombamento = new Tombamento();
		listTombamento = new ArrayList<>();
		listEquipamentoInventario = new ArrayList<>();

		servidor = (Servidor) usuarioSessao.recuperarServidor();

		List<LocalInventario> locais = new ArrayList<>();
		locais = daoLocalInventario.listar(LocalInventario.class, " servidorConferencia = '" + servidor.getId() + "'");
		List<Permissao> listaPermissao = daoPermissao.listar(Permissao.class, " servidor ='" + servidor.getId() + "'");
		chefe = false;

		for (Permissao p : listaPermissao) {
			if (p.getTipo().getDescricao().equals("inventario"))
				chefe = true;
		}

		if (chefe == false) {

			if (locais.size() == 0) {
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect("../negadoinventario.jsf");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void carregarListaEquipamentos() {
		listEquipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
				" localInventario = '" + localInventarioSelecionado.getId() + "'");
	}

	public List<Localidades> getListLocal() {
		return listLocal;
	}

	public void setListLocal(List<Localidades> listLocal) {
		this.listLocal = listLocal;
	}

	public void buscarTombamento() {

		listTombamento = daoTombamento.listar(Tombamento.class, " codigo = '" + tombamento.getCodigo() + "'");
		if (listTombamento.size() == 0)
			ExibirMensagem.exibirMensagem(Mensagem.TOMBAMENTO);

		else {

			List<EquipamentoInventario> listEquipamentoInventario = daoEquipamentoInventario
					.listar(EquipamentoInventario.class, " localInventario = '" + localInventarioSelecionado.getId()
							+ "' and tombamento = '" + listTombamento.get(0).getId() + "'");
			if (listEquipamentoInventario.size() > 0)
				ExibirMensagem.exibirMensagem(Mensagem.VERIFICADOTOMBAMENTO);

			else
				tombamento = listTombamento.get(0);
			equipamentoInventario.setTrocarEtiqueta("não");

		}

	}

	public void inativar(EquipamentoInventario equipamentoInventario) {
		try {
			equipamentoInventario.setStatus(false);
			equipamentoInventarioService.inserirAlterar(equipamentoInventario);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		carregarListaEquipamentos();
	}

	public void salvarEquipamento() {

		if (tombamento.getLocal().getId() == localInventarioSelecionado.getLocal().getId())
			equipamentoInventario.setPertenceLocal(true);
		else
			equipamentoInventario.setPertenceLocal(false);

		equipamentoInventario.setDataConferencia(new Date());

		equipamentoInventario.setLocalInventario(localInventarioSelecionado);
		equipamentoInventario.setStatus(true);
		equipamentoInventario.setTombamento(tombamento);

		equipamentoInventarioService.inserirAlterar(equipamentoInventario);
		carregarListaEquipamentos();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		criarNovoObjeto();

	}

	public List<Inventario> completarInventario(String str) {

		List<Inventario> listInventario = new ArrayList<>();
		listInventario = daoInventario.listaComStatus(Inventario.class);
		List<Inventario> listInventarioAdd = new ArrayList<>();
		for (Inventario at : listInventario) {
			if (at.getDescricao().toLowerCase().startsWith(str)) {
				listInventarioAdd.add(at);
			}
		}
		return listInventarioAdd;
	}

	public List<LocalInventario> completaLocalInventario() {

		List<Localidades> listLocal = new ArrayList<>();

		if (chefe)
			return daoLocalInventario.listar(LocalInventario.class, " Inventario = '" + inventario.getId() + "'");
		else
			return daoLocalInventario.listar(LocalInventario.class,
					" Inventario = '" + inventario.getId() + "' and servidorConferencia = '" + servidor.getId() + "'");

	}

	public List<LocalInventario> completarLocal(String str) {

		List<LocalInventario> listLocalAdd = new ArrayList<>();
		for (LocalInventario at : completaLocalInventario()) {
			if (at.getLocal().getDescricao().toLowerCase().startsWith(str)) {
				listLocalAdd.add(at);
			}
		}
		return listLocalAdd;
	}

	public void criarNovoObjeto() {
		listTombamento.clear();
		equipamentoInventario = new EquipamentoInventario();
		tombamento = new Tombamento();

	}

	public LocalInventario getLocalInventarioSelecionado() {
		return localInventarioSelecionado;
	}

	public void setLocalInventarioSelecionado(LocalInventario localInventarioSelecionado) {
		this.localInventarioSelecionado = localInventarioSelecionado;
	}

	public EquipamentoInventario getEquipamentoInventario() {
		return equipamentoInventario;
	}

	public void setEquipamentoInventario(EquipamentoInventario equipamentoInventario) {
		this.equipamentoInventario = equipamentoInventario;
	}

	public Inventario getInventario() {
		return inventario;
	}

	public void setInventario(Inventario inventario) {
		this.inventario = inventario;
	}

	public Tombamento getTombamento() {
		return tombamento;
	}

	public EstadoConservacao[] getEstadoConservacao() {
		return EstadoConservacao.values();
	}

	public List<EquipamentoInventario> getListEquipamentoInventario() {
		return listEquipamentoInventario;
	}

	public void setListEquipamentoInventario(List<EquipamentoInventario> listEquipamentoInventario) {
		this.listEquipamentoInventario = listEquipamentoInventario;
	}

	public void setTombamento(Tombamento tombamento) {
		this.tombamento = tombamento;
	}

}
