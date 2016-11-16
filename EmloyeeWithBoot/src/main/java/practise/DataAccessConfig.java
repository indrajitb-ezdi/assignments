package practise;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@ComponentScan("practise")
public class DataAccessConfig {
	private String dbPropertiesFile = "/application.properties"; 
	private Properties properties = null;

	/*@Autowired
	@Bean
	public SessionFactory sessionFactory(DataSource dataSource) {
		
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
//		sessionBuilder.setProperties(getConnProperties());
		sessionBuilder.setProperty("dialect", getConnProperties().getProperty("dialect"));
		sessionBuilder.addAnnotatedClass(demo.mvc.Employee.class);
		return sessionBuilder.buildSessionFactory();
	}*/
	
	/*@Bean
	public DataSource getDataSource() {
		Properties properties = getConnProperties();
		DriverManagerDataSource source = new DriverManagerDataSource();
		source.setDriverClassName(properties.getProperty("driver_class"));
		source.setUrl(properties.getProperty("url"));
		source.setUsername(properties.getProperty("username"));
		source.setPassword(properties.getProperty("password"));
		return source;
	}*/
	
//	@Autowired
	@Bean
	public SessionFactory sessionFactory(DataSource dataSource) {
		
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
//		sessionBuilder.setProperties(getConnProperties());
		sessionBuilder.setProperty("dialect", getConnProperties().getProperty("hibernate.dialect"));
		sessionBuilder.addAnnotatedClass(practise.Employee.class);
		return sessionBuilder.buildSessionFactory();
	}
	
	private Properties getConnProperties() {
		if(properties != null)
			return properties;
		
		Properties properties = new Properties();
		InputStream input = null;
		try {
//			input = new FileInputStream(dbPropertiesFile);
			input = DataAccessConfig.class.getResourceAsStream(dbPropertiesFile);
			properties.load(input);
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(input != null) {
				try {
					input.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
	
	/*@Bean
    public HibernateTransactionManager getTransactionManager() {
        return new HibernateTransactionManager(sessionFactory());    
    }*/
}
