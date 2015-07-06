package test.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_CLIENTI")
@NamedQueries({
	@NamedQuery(name = "clientiByCompagnia", query = "SELECT o FROM Cliente o WHERE o.compagnia.nome = :nomeCompagnia")
})
@SuppressWarnings("serial")
public class Cliente implements Serializable	{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "nome")
	private String nome;

	@ManyToOne
	@JoinColumn(name = "COMPAGNIA")
	@Column(name = "COMPAGNIA")
	private Compagnia compagnia;

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

	public Compagnia getCompagnia() {
		return compagnia;
	}

	public void setCompagnia(Compagnia compagnia) {
		this.compagnia = compagnia;
	}

}
