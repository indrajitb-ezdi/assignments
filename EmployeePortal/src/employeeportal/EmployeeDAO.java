package employeeportal;

import java.util.List;

import java.util.Iterator; 
 
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class EmployeeDAO {
	private static SessionFactory factory = null;
	
	private static SessionFactory getFactory() {
		if(factory != null) 
			return factory;
		try{
	        //factory = new Configuration().configure().buildSessionFactory();
	    	Configuration configuration = new Configuration();
	    	configuration.configure();
	    	StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
	    		configuration.getProperties()).build();
	    	factory = configuration.buildSessionFactory(serviceRegistry);
	    }catch (Throwable ex) { 
	        System.err.println("Failed to create sessionFactory object." + ex);
	        throw new ExceptionInInitializerError(ex);
	    }
		return factory;
	}
	
	/* Method to CREATE an employee in the database */
	public Integer addEmployee(String fname, String lname, int salary){
		factory = getFactory();
	    Session session = factory.openSession();
	    Transaction tx = null;
	    Integer employeeID = null;
	    try{
	        tx = session.beginTransaction();
	        Employee employee = new Employee(fname, lname, salary);
	        employeeID = (Integer) session.save(employee); 
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	        session.close(); 
	    }
	    return employeeID;
	}
	
	/* Method to  READ all the employees */
//	@SuppressWarnings("unchecked")
	public List<Employee> listEmployees(){
		factory = getFactory();
		Session session = factory.openSession();
	    Transaction tx = null;
	    List<Employee> employees = null;
	    try{
	    	tx = session.beginTransaction();
	        employees = session.createQuery("FROM Employee").list(); 
//	        for (Iterator iterator = 
//	                           employees.iterator(); iterator.hasNext();){
//	        	Employee employee = (Employee) iterator.next(); 
//	            System.out.print("First Name: " + employee.getFirstName()); 
//	            System.out.print("  Last Name: " + employee.getLastName()); 
//	            System.out.println("  Salary: " + employee.getSalary()); 
//	        }
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	        session.close(); 
	    }
	    return employees;
	}
	
	/* Method to retrieve single employee */
	public Employee getEmployee(int empId) {
		factory = getFactory();
		Session session = factory.openSession();
	    Transaction tx = null;
	    Employee employee = null;
	    try {
	    	tx = session.beginTransaction();
	    	employee = (Employee) session.createQuery("FROM Employee where id = " + empId).list().get(0);
	    	tx.commit();
	    }
	    catch(HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }
	    finally {
	        session.close(); 
	    }
	    return employee;
	}
	
	/* Method to UPDATE salary for an employee */
	public boolean updateEmployee(Integer EmployeeID, String firstName, String lastName, int salary ){
		factory = getFactory();
		boolean status = false;
		Session session = factory.openSession();
	    Transaction tx = null;
	    try{
	        tx = session.beginTransaction();
	        Employee employee = 
	                    (Employee)session.get(Employee.class, EmployeeID); 
	        employee.setFirstName(firstName);
	        employee.setLastName(lastName);
	        employee.setSalary(salary);
			session.update(employee); 
	        tx.commit();
	        status = true;
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	        session.close(); 
	    }
	    return status;
	}
	
	/* Method to DELETE an employee from the records */
	public boolean deleteEmployee(Integer EmployeeID) {
		factory = getFactory();
		boolean status = false;
		Session session = factory.openSession();
	    Transaction tx = null;
	    try{
	        tx = session.beginTransaction();
	        Employee employee = 
	                   (Employee)session.get(Employee.class, EmployeeID); 
	        session.delete(employee); 
	        tx.commit();
	        status = true;
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	        session.close(); 
	    }
	    return status;
	}
}
