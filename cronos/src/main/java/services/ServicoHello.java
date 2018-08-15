package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.lucene.analysis.core.TypeTokenFilter;
import org.glassfish.hk2.api.ServiceLocator;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.primefaces.mobile.component.footer.Footer;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import ac.modelo.Pessoa;
import base.modelo.Servidor;
import dao.GenericDAO;
import inventario.modelo.Equipamento;
import inventario.modelo.EquipamentoInventario;
import inventario.modelo.Inventario;
import inventario.modelo.LocalInventario;
import inventario.modelo.Tombamento;
import inventario.service.EquipamentoInventarioService;
import inventario.service.EquipamentoService;
import inventario.service.TombamentoService;
import util.CriptografiaSenha;
import util.ExibirMensagem;

@Path("/service")
public class ServicoHello {

	// Operacoes operacoes = new Operacoes();

	//
	// @Inject
	// private Operacoes operacoes;
	//

	private LocalInventario localInventario = new LocalInventario();

	@Inject
	private GenericDAO<Servidor> daoServidor;

	@Inject
	private GenericDAO<Inventario> daoInventario;

	@Inject
	private GenericDAO<Tombamento> daoTombamento;

	@Inject
	private GenericDAO<Equipamento> daoEquipamento;

	@Inject
	private GenericDAO<EquipamentoInventario> daoEquipamentoInventario;

	@Inject
	private EquipamentoInventarioService equipamentoInventarioService;

	@Inject
	private TombamentoService tombamentoService;

	@Inject
	private EquipamentoService equipamentoService;

	@Inject
	private GenericDAO<LocalInventario> daoLocaisInventario;
	// @PostConstruct
	// public void inicializar() {
	//
	// }

	// http://localhost:8080/cronos/rest/service/
	// http://localhost:8080/cronos/rest/service/logar?login=adreluu&senha=111
	// http://localhost:8080/hellows/rest/service/somarInteiros?valor=1&valor2=3

	/*
	 * @POST
	 * 
	 * @Produces("application/json; charset=UTF-8")
	 * 
	 * @Consumes({ "application/json; charset=UTF-8" })
	 * 
	 * @Path("/logar") public String verificaLogin(Servidor servidor) {
	 * 
	 * List<Servidor> listServidor = new ArrayList<>(); listServidor =
	 * daoServidor.listar(Servidor.class, " usuario = '" + login + "'");
	 * 
	 * if (listServidor.size() > 0) {
	 * 
	 * if (CriptografiaSenha.decptrografar(senha,
	 * listServidor.get(0).getSenha())) { return ""+listServidor.get(0).getId();
	 * } else return ""+0;
	 * 
	 * } else { return ""+0; }
	 * 
	 * }
	 */

	@POST
	@Produces("application/json; charset=UTF-8")
	@Consumes({ "application/json; charset=UTF-8" })
	@Path("/logar/{login}/{senha}")
	public String verificaLogin(@PathParam("login") String login, @PathParam("senha") String senha) {

		List<Servidor> listServidor = new ArrayList<>();
		listServidor = daoServidor.listar(Servidor.class, " usuario = '" + login + "'");

		if (listServidor.size() > 0) {

			if (CriptografiaSenha.decptrografar(senha, listServidor.get(0).getSenha())) {
				return "" + listServidor.get(0).getId();
			} else
				return "" + 0;

		} else {
			return "" + 0;
		}

	}

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/inventarios")
	public Response inventario() {

		List<Inventario> listaInventario = daoInventario.listaComStatus(Inventario.class);
		GenericEntity<List<Inventario>> lista = new GenericEntity<List<Inventario>>(listaInventario) {
		};

		return Response.status(200).entity(lista).build();

	}

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/locaisInventario/{idInventario}/{idServidor}")
	public Response LocaisInventario(@PathParam("idInventario") Integer idInventario,
			@PathParam("idServidor") Integer idServidor) {

		List<LocalInventario> listaInventario = daoLocaisInventario.listar(LocalInventario.class,
				" Inventario = '" + idInventario + "' and servidorConferencia = '" + idServidor + "'");

		GenericEntity<List<LocalInventario>> lista = new GenericEntity<List<LocalInventario>>(listaInventario) {
		};

		return Response.status(200).entity(lista).build();

	}

	@POST
	@Produces("application/json; charset=UTF-8")
	@Consumes({ "application/json; charset=UTF-8" })
	@Path("/locaisConferir/{idInventario}/{idServidor}")
	public Response locaisConferir(@PathParam("idInventario") Integer idInventario,
			@PathParam("idServidor") Integer idServidor) {

		List<LocalInventario> listaLocalInventario = daoLocaisInventario.listar(LocalInventario.class,
				" Inventario = '" + idInventario + "' and servidorConferencia = '" + idServidor + "'");

		GenericEntity<List<LocalInventario>> lista = new GenericEntity<List<LocalInventario>>(listaLocalInventario)

		{
		};

		return Response.status(200).entity(lista).build();

	}

	/*
	 * @POST
	 * 
	 * @Produces("application/json; charset=UTF-8")
	 * 
	 * @Consumes({ "application/json; charset=UTF-8" })
	 * 
	 * @Path("/locaisConferir/{idInventario}/{idServidor}") public Response
	 * locaisConferir(@PathParam("idInventario") Integer idInventario,
	 * 
	 * @PathParam("idServidor") Integer idServidor) {
	 * 
	 * List<LocalInventario> listaLocalInventario =
	 * daoLocaisInventario.listar(LocalInventario.class, " Inventario = '" +
	 * idInventario + "' and servidorConferencia = '" + idServidor + "'");
	 * 
	 * GenericEntity<List<LocalInventario>> lista = new
	 * GenericEntity<List<LocalInventario>>(listaLocalInventario) { };
	 * 
	 * return Response.status(200).entity(lista).build();
	 * 
	 * }
	 */

	@POST
	@Produces("application/json; charset=UTF-8")
	@Consumes({ "application/json; charset=UTF-8" })
	@Path("/listaConferencia")
	public String listaConferencia(List<EquipamentoInventario> lista) {

		Gson gson = new Gson();

		String listaString = gson.toJson(lista, new TypeToken<List<EquipamentoInventario>>() {
		}.getType());

		List<EquipamentoInventario> listasConvertida = gson.fromJson(listaString,
				new TypeToken<List<EquipamentoInventario>>() {
				}.getType());

		List<Tombamento> listaTombamento = daoTombamento.listaComStatus(Tombamento.class);
		List<EquipamentoInventario> listaEquipamentoInventario = daoEquipamentoInventario
				.listaComStatus(EquipamentoInventario.class);

		Tombamento t;
		for (EquipamentoInventario e : listasConvertida) {
			
			localInventario = e.getLocalInventario();
			

			boolean salvar = true;
			// esse for apenas verifica se esse tombamento já foi adicionado
			// para esse inventário, se foi ele apenas ignora, caso não tenha
			// sido conferido ele insere
			for (EquipamentoInventario equipamento : listaEquipamentoInventario) {

				System.out.println("equipamento " + e.getTombamento().getCodigo());

				if (equipamento.getTombamento().getCodigo().toString().equals(e.getTombamento().getCodigo().toString())

						&& ((equipamento.getLocalInventario().getInventario().getId() == e.getLocalInventario()
								.getInventario().getId())
								|| (equipamento.getLocalInventario().getInventario().getId()
										.equals(e.getLocalInventario().getInventario().getId())))

						&& equipamento.getLocalInventario().getLocal().getId() == e.getLocalInventario().getLocal()
								.getId()

						&& equipamento.isStatus() == true) {

					salvar = false;
				}
 

			}
 

			if (e.getTombamento().getCodigo().equals(""))
				salvar = true;

			// parte de teste

			if (salvar) {
				t = new Tombamento();
				// apenas busca o tombamento para ver se é um tombado ou não.
				for (Tombamento tombamento : listaTombamento) {
					if (tombamento.getCodigo().equals(e.getTombamento().getCodigo())) {
						t = tombamento;
						break;
					}
				}

				boolean cadastrar = true;
				if (e.getTombamento().getCodigo().equals("")) {

					t = new Tombamento();
					cadastrar = cadastraNaoTombado(e);
				}

				// se for encontrado nos tombamentos entra aqui.
				if (cadastrar) {
					
					if (t.getId() != null) {
						System.out.println("entro no if ");
						List<EquipamentoInventario> listEquipamentoAntigo = daoEquipamentoInventario.listar(
								EquipamentoInventario.class,
								" tombamento.codigo = '" + t.getCodigo() + "' and localInventario.Inventario = '"
										+ e.getLocalInventario().getInventario().getId()
										+ "' and localInventario.local != '" + e.getLocalInventario().getLocal().getId()
										+ "'");

						for (EquipamentoInventario eq : listEquipamentoAntigo) {
							eq.setBensEmDuplicidade(true);
							equipamentoInventarioService.inserirAlterar(eq);
						}

						if (listEquipamentoAntigo.size() > 0)
							e.setBensEmDuplicidade(true);
						else
							e.setBensEmDuplicidade(false);

						e.setDataConferencia(retornaData(e.getDataConferenciaFormatada()));

						if (e.getLocalInventario().getLocal().getId() == t.getLocal().getId())
							e.setPertenceLocal(true);
						else
							e.setPertenceLocal(false);

						e.setTombamento(t);

						if (e.getTrocarEtiqueta().equals("n?o"))
							e.setTrocarEtiqueta("não");

						e.setNaoTombado(false);
						e.setId(null);

						equipamentoInventarioService.inserirAlterar(e);

					} else {
						System.out.println("entro no else");
						// se ele é um não tombado entra aqui nesse código.
						e.setId(null);
						e.setDataConferencia(retornaData(e.getDataConferenciaFormatada()));

						e.setPertenceLocal(true);
						e.setNaoTombado(true);

						Equipamento equipamentos = new Equipamento();
						equipamentos.setStatus(true);
						equipamentos.setDescricao(e.getTombamento().getDescricao());
						e.getTombamento().setEquipamento(equipamentos);

						if (e.getTrocarEtiqueta().equals("n?o"))
							e.setTrocarEtiqueta("não");

						 
						equipamentoService.inserirAlterar(e.getTombamento().getEquipamento());
					    Tombamento tombamento = new Tombamento();
					    tombamento = e.getTombamento();
					    tombamento.setId(null);
						
						
						
						tombamentoService.inserirAlterar(tombamento);
						e.setTombamento(tombamento);
						e.setBensEmDuplicidade(false);
						e.setId(null);

						equipamentoInventarioService.inserirAlterar(e);

					}
				}
			}
			 
		}
		
		List<EquipamentoInventario> listaEqupamentoInventarioDeletar = new ArrayList<>();
		listaEqupamentoInventarioDeletar = daoEquipamentoInventario.listar(EquipamentoInventario.class, " localInventario = '"+localInventario.getId()+"'");
		
		for(EquipamentoInventario l : listaEqupamentoInventarioDeletar){
			 
			boolean excluir = true;
			for(EquipamentoInventario web : listasConvertida){ 
				if(web.getTombamento().getCodigo().equals(l.getTombamento().getCodigo())){
					System.out.println("não vai excluir "+web.getId());
					excluir = false;
				}
				 
			}
			if(excluir)
				l.setStatus(false);
				l.setDataRemovido(new Date());
				equipamentoInventarioService.inserirAlterar(l);
		}	

		return "";

	}

	public boolean cadastraNaoTombado(EquipamentoInventario equipamento) {

		List<EquipamentoInventario> listaEquipamentoInventario = daoEquipamentoInventario.listar(
				EquipamentoInventario.class,
				" localInventario.Inventario ='" + equipamento.getLocalInventario().getInventario().getId() + "'");

		for (EquipamentoInventario naoTombado : listaEquipamentoInventario) {

			if (equipamento.getTombamento().getNumeroSerie().equals(naoTombado.getTombamento().getNumeroSerie())
					&& equipamento.getLocalInventario().getId() == naoTombado.getLocalInventario().getId()) {

				return false;
			}
		}
		return true;
	}

	public Date retornaData(String data) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(data);
			return date;
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return new Date();
	}

	// http://localhost:8080/hellows/rest/service/somarInteiros?valor=1&valor2=3
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/somarInteiros")
	public String helloWebService(@QueryParam("valor") Integer valor, @QueryParam("valor2") Integer valor2) {
		// return (valor+valor2);
		return "" + (valor + valor2);

	}

	// http://localhost:8080/hellows/rest/service/hello?valor=1&valor2=3
	@GET
	@Produces("application/json; charset=UTF-8")
	// @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
	@Path("/listaTimesSP")
	public Response listaTimes() {

		List<Time> lt = new ArrayList<>();
		lt.add(new Time("Palmeiras - Maior Campeão Brasileiro", "São Paulo"));
		lt.add(new Time("São Paulo", "São Paulo"));
		lt.add(new Time("Corinthians", "São Paulo"));
		lt.add(new Time("Santos", "São Paulo"));
		lt.add(new Time("Ituano", "São Paulo"));
		lt.add(new Time("Linense", "São Paulo"));
		lt.add(new Time("aaaaa", "São Paulo"));

		GenericEntity<List<Time>> lista = new GenericEntity<List<Time>>(lt) {
		};
		// return lt;
		return Response.status(200).entity(lista).build();
	}

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/listaTimesRio")
	public List<Time> listaTimes2() {

		List<Time> lt = new ArrayList<>();

		lt.add(new Time("Flamengo", "Rio de Janeiro"));
		lt.add(new Time("Vasco", "Rio de Janeiro"));

		// GenericEntity<List<Time>> lista = new
		// GenericEntity<List<Time>>(lt){};
		return lt;
		// return Response.status(200).entity(lista).build();
	}

	// http://localhost:8080/hellows/rest/service/hello2/10/10
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/hello2/{valor}/{valor2}")
	public String helloWebService2(@PathParam("valor") Integer valor, @PathParam("valor2") Integer valor2) {
		return "Olá Mundo WebService " + (valor + valor2);
	}

	// http://localhost:8080/hellows/rest/service/hello3/10/
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/hello3/{valor}")
	public String helloWebService3(@PathParam("valor") Integer valor) {
		return "Olá Mundo WebService " + valor;
	}

}
