package cope.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cope.modelo.enums.StatusSubmissao;

@Entity
@Table(name = "tab_criterios")
public class Criterios implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_criterios")
	private Long id;
	private String descricao;
	
	private boolean status;
	
	private String criterio;

	public Long getId() {
		return id;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}
 
 

}
