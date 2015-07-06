package test.web;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import test.dao.TestDao;
import test.entities.Cliente;

/*
 * http://localhost:9080/TestJPAWeb/test
 * 
 */
@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {

	ApplicationContext ctx;
	
    @Override
    public void init() throws ServletException {
    	super.init();
    	ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		TestDao dao = (TestDao)ctx.getBean("testDaoJpa");

		try{
		
			dao.insertSomeData();
			
			List<Cliente> clienti = dao.getClientiByCompagnia("siletto.it");

			try{
				dao.testTransazioneFallita();
			}catch(RuntimeException e){}
			
			response.getOutputStream().print("success\n");
			for (Cliente cliente : clienti) {
				response.getOutputStream().print(cliente.getNome() + " - "+ cliente.getCompagnia().getNome() + "\n");
			}

		
		}catch(Exception e){
			response.getOutputStream().print("failure\n");
			e.printStackTrace(new PrintStream(response.getOutputStream()));
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
