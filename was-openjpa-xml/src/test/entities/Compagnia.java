package test.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

public class Compagnia implements Serializable	{

	private Long id;
	private String nome;
	private List<Cliente> clienti;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Cliente> getClienti() {
		return clienti;
	}

	public void setClienti(List<Cliente> clienti) {
		this.clienti = clienti;
	}


	
}
