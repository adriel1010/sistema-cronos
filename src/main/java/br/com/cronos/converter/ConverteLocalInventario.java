package br.com.cronos.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
 
import dao.GenericDAO; 
import inventario.modelo.LocalInventario;
import util.Mensagem;

@Named("converteLocalInventario")
public class ConverteLocalInventario implements Converter {

	
	@Inject
	private GenericDAO<LocalInventario> dao;
	
	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				
				return  dao.buscarPorId(LocalInventario.class, Long.parseLong(value));
			
			} catch (Exception e) {
				e.printStackTrace();
				throw new ConverterException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO_CONVERTER, ""));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if (object != null) {
			LocalInventario cur = (LocalInventario) object;
			return String.valueOf(cur.getId());
		} else {
			return null;
		}
	}
}
