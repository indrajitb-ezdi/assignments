package employeeportal;

import java.io.IOException;
import java.io.PrintWriter;
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
//		ServletContext context = getServletContext();
		
		// Link to add employee
		out.println("<a href='AddEditEmployee'>Add New Employee</a>");
		
		// Display list of employees
		List<Employee> empList = empDAO.listEmployees();
		
		out.println("<h2>Employees List</h2>");
		out.print("<table border='1' width='50%'");
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
			out.print("<td align='center'>"
					+ "<form name='editForm' method='post' action='AddEditEmployee'>"
					+ "<input type='hidden' name='id' value='" + emp.getId() + "' />"
					+ "<input type='submit' value='Edit'/>"
					+ "</form>"
					+ "</td>");
			out.print("<td align='center'>"
					+ "<form name='deleteForm' method='post' action='DeleteEmployee'>"
					+ "<input type='hidden' name='id' value='" + emp.getId() + "' />"
					+ "<input type='submit' value='Delete'/>"
					+ "</form>"
					+ "</td>");
			out.print("</tr>");
		}
		
		out.close();
	}
}
