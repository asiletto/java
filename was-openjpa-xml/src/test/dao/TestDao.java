package test.dao;

import java.util.List;

import test.entities.Cliente;

public interface TestDao {

	public void insertSomeData();
	
	public List<Cliente> getClientiByCompagnia(String compagnia);

	public void testTransazioneFallita();
}
