package test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;

@ApplicationPath("rest")
public class Application extends javax.ws.rs.core.Application {

	protected Set<Object> singletons;
	
	public Application() {
		singletons = new HashSet<Object>();
		
		Map<String,Object> resouces = StaticSpringContext.getContext().getBeansWithAnnotation(Path.class);
		for (String name : resouces.keySet())
			singletons.add( resouces.get(name) );

	}

	public Set<Object> getSingletons() {
		return singletons;
	}
	
}
