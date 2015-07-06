package test.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import test.dao.TestDao;
import test.entities.Cliente;
import test.entities.Compagnia;

@Repository("testDaoJpa")
public class TestDaoJpa implements TestDao {
	
	@PersistenceContext(unitName = "TestJPAPersistence")
    protected EntityManager entityManager;
	
	@Override
	@Transactional
	public void insertSomeData() {

		Compagnia unipol = new Compagnia();
		unipol.setNome("Unipol");
		
		Cliente mario = new Cliente();
		mario.setNome("Mario Rossi");
		mario.setCompagnia(unipol);

		Cliente luigi = new Cliente();
		luigi.setNome("Luigi Verdi");
		luigi.setCompagnia(unipol);

		entityManager.persist(unipol);
		entityManager.persist(mario);
		entityManager.persist(luigi);

	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Cliente> getClientiByCompagnia(String compagnia) {
		TypedQuery<Cliente> query = entityManager.createNamedQuery("clientiByCompagnia", Cliente.class);
		query.setParameter("nomeCompagnia", compagnia);
		return query.getResultList();
	}

	@Override
	@Transactional
	public void testTransazioneFallita() {
		Compagnia unipol = (Compagnia)entityManager.createNamedQuery("findAllCompagnie").getSingleResult();
		entityManager.remove(unipol);
		throw new RuntimeException("test transazione");
	}
	
	/*
		entityManager.persist(newIstance);
		entityManager.find(entityClass, id);
		entityManager.merge(transientObject);
        entityManager.remove(persistenceObject);
    }*/
}
