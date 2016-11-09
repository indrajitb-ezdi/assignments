package demo.mvc;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	/*@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}*/
	/*private static SessionFactory factory = null;

	private static SessionFactory getFactory() {
		if (factory != null)
			return factory;
		try {
			// factory = new Configuration().configure().buildSessionFactory();
			Configuration configuration = new Configuration();
			configuration.configure();
			configuration.addAnnotatedClass(demo.mvc.Employee.class);
			StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			factory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		return factory;
	}

	public static void printFactory(SessionFactory factory) {
		Map<String, ClassMetadata> map = factory.getAllClassMetadata();

		System.out.println("Printing register classes");
		for (Map.Entry<String, ClassMetadata> entry : map.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
	}*/

	/* Method to CREATE an employee in the database */
	public Integer addEmployee(String fname, String lname, int salary) {
//		factory = getFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Integer employeeID = null;
		try {
			tx = session.beginTransaction();
			Employee employee = new Employee(fname, lname, salary);
			employeeID = (Integer) session.save(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employeeID;
	}

	/* Method to READ all the employees */
	@SuppressWarnings("unchecked")
	public List<Employee> listEmployees() {
//		factory = getFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Employee> employees = null;
		try {
			tx = session.beginTransaction();
			employees = session.createQuery("FROM demo.mvc.Employee").list();
			// for (Iterator iterator =
			// employees.iterator(); iterator.hasNext();){
			// Employee employee = (Employee) iterator.next();
			// System.out.print("First Name: " + employee.getFirstName());
			// System.out.print(" Last Name: " + employee.getLastName());
			// System.out.println(" Salary: " + employee.getSalary());
			// }
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employees;
	}

	/* Method to retrieve single employee */
	public Employee getEmployee(int empId) {
//		factory = getFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Employee employee = null;
		try {
			tx = session.beginTransaction();
			// employee = (Employee) session.createQuery("FROM Employee where id
			// = " + empId).list().get(0);
			employee = (Employee) session.get(Employee.class, empId);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employee;
	}

	/* Method to UPDATE salary for an employee */
	public boolean updateEmployee(Integer EmployeeID, String firstName, String lastName, int salary) {
//		factory = getFactory();
		boolean status = false;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, EmployeeID);
			if (employee == null)
				throw new NullPointerException();
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employee.setSalary(salary);
			session.update(employee);
			tx.commit();
			status = true;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} catch (NullPointerException e) {
			if (tx != null)
				tx.rollback();
			status = false;
		} finally {
			session.close();
		}
		return status;
	}

	/* Method to DELETE an employee from the records */
	public boolean deleteEmployee(Integer EmployeeID) {
//		factory = getFactory();
		boolean status = false;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, EmployeeID);
			if (employee == null)
				throw new NullPointerException();
			session.delete(employee);
			tx.commit();
			status = true;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} catch (NullPointerException e) {
			if (tx != null)
				tx.rollback();
			status = false;
		} finally {
			session.close();
		}
		return status;
	}
}
