package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class StaticSpringContext implements ApplicationContextAware {

	private static ApplicationContext appContext;

	private StaticSpringContext() {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		appContext = applicationContext;
	}

	public static ApplicationContext getContext() {
		return appContext;
	}

}