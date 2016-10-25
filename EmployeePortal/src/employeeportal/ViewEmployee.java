package employeeportal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(
		name = "ViewEmployee",
		urlPatterns = {"/ViewEmployee"},
		loadOnStartup=1
)
public class ViewEmployee extends HttpServlet {
	private EmployeeDAO empDAO;
	
	public  ViewEmployee() {
		empDAO = new EmployeeDAO();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		// TODO Link to add employee
		out.println("<a href=''>Add New Employee</a>");
		
		// TODO Display list of employees
		List<Employee> empList = empDAO.listEmployees();
		
		out.println("<h2>Employees List</h2>");
		out.print("<table border='1' width='100%'");
		out.print("<tr>"
				+ "<th>First name</th>"
				+ "<th>Last name</th>"
				+ "<th>Salary</th>"
				+ "<th>Edit</th>"
				+ "<th>Delete</th>"
				+ "</tr>");
		for(Employee emp: empList) {
			out.print("<tr>");
			out.print("<td>" + emp.getFirstName() + "</td>");
			out.print("<td>" + emp.getLastName() + "</td>");
			out.print("<td>" + emp.getSalary() + "</td>");
			out.print("<td><a href=''>Edit</a></td>");
			out.print("<td><a href=''>Delete</a></td>");
			out.print("</tr>");
		}
		
		out.close();
	}
}
