package employeeportal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(
		name = "SaveEmployee",
		urlPatterns = {"/SaveEmployee"},
		loadOnStartup=1
)
public class SaveEmployee extends HttpServlet {
	private EmployeeDAO empDAO;
	
	public SaveEmployee() {
		empDAO = new EmployeeDAO();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Perform save operation
		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		int salary;
		if(request.getParameter("salary") == null || request.getParameter("salary").equals(""))
			salary = 0;
		else
			salary = Integer.parseInt(request.getParameter("salary"));
		
		Integer id = null;
		
		if(request.getParameterMap().containsKey("id") && request.getParameter("id").length() > 0) {
			// Update existing employee
			id = Integer.parseInt(request.getParameter("id"));
			boolean succeded = empDAO.updateEmployee(id, firstName, lastName, salary);
			if(!succeded)
				id = null;
		}
		else {
			// Add new employee
			id = empDAO.addEmployee(firstName, lastName, salary);
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		if(id != null) {
			out.println("<h2>Employee Details</h2>");
			out.print("<table border='0'");
			out.print("<tr><td>");
			out.print("<h4>First Name:</h4>");
			out.print("</td><td><h2>");
			out.print(firstName);
			out.print("</h2></td></tr>");
			out.print("<tr><td>");
			out.print("<h4>Last Name:</h4>");
			out.print("</td><td><h2>");
			out.print(lastName);
			out.print("</h2></td></tr>");
			out.print("<tr><td>");
			out.print("<h4>Salary:</h4>");
			out.print("</td><td><h2>");
			out.print(salary);
			out.print("</h2></td></tr>");
			out.print("</table>");
			out.println("<h2>Saved successfully ...</h2>");
		}
		else {
			out.println("<h2>Employee details could not be saved</h2>");
		}
		out.print("<a href='ViewEmployee'>Continue</a>");
		out.close();
	}
}
