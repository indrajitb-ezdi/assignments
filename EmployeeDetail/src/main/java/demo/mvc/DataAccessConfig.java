package demo.mvc;

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
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;


@Configuration
@EnableWebMvc
@ComponentScan("demo.mvc")
public class DataAccessConfig {
	/*private String dbPropertiesFile = "/home/local/EZDI/indrajit.b/Workspace/git/assignments/EmployeeDetail/src/main/"
			+ "resources/database.properties";*/ 
	private String dbPropertiesFile = "/database.properties";
	private Properties properties = null;

	@Autowired
	@Bean
	public SessionFactory sessionFactory(DataSource dataSource) {
		
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
//		sessionBuilder.setProperties(getConnProperties());
		sessionBuilder.setProperty("dialect", getConnProperties().getProperty("dialect"));
		sessionBuilder.addAnnotatedClass(demo.mvc.Employee.class);
		return sessionBuilder.buildSessionFactory();
	}
	
	@Bean
	public DataSource getDataSource() {
		Properties properties = getConnProperties();
		DriverManagerDataSource source = new DriverManagerDataSource();
		source.setDriverClassName(properties.getProperty("driver_class"));
		source.setUrl(properties.getProperty("url"));
		source.setUsername(properties.getProperty("username"));
		source.setPassword(properties.getProperty("password"));
		return source;
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
}
