package employeeportal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.sun.corba.se.spi.orbutil.fsm.FSMImpl;

@WebServlet(
		name = "AddEditEmployee",
		urlPatterns = {"/AddEditEmployee"},
		loadOnStartup=1
)
public class AddEditEmployee extends HttpServlet {
	private EmployeeDAO empDAO;
	
	public AddEditEmployee() {
		empDAO = new EmployeeDAO();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		displayAddForm(out);
		out.close();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Edit operation
		String empId = request.getParameter("id");
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		displayEditForm(out, empId);
		out.close();
	}
	
	private void displayAddForm(PrintWriter out) {
		displayForm(out, "", "", "", "");
	}
	
	private void displayEditForm(PrintWriter out, String id) {
		Employee emp = empDAO.getEmployee(Integer.parseInt(id));
		if(emp == null)
			displayForm(out, "", "", "", "");
		else
			displayForm(out, Integer.toString(emp.getId()), emp.getFirstName(), emp.getLastName(), Integer.toString(emp.getSalary()));
	}
	
	private void displayForm(PrintWriter out, String id, String firstName, String lastName, String salary) {
		out.println("<h2>Enter Employee Detail</h2>");
		out.print("<form action='SaveEmployee' method='post'>");
		out.print("<input type='hidden' name='id' value='" + id + "'");
		out.print("<table border='0'");
		out.print("<tr><td>");
		out.print("<h4>First Name:</h4>");
		out.print("</td><td>");
		out.print("<input type='text' name='first_name' value='" + firstName + "'/>");
		out.print("</td></tr>");
		out.print("<tr><td>");
		out.print("<h4>Last Name:</h4>");
		out.print("</td><td>");
		out.print("<input type='text' name='last_name' value='" + lastName + "'/>");
		out.print("</td></tr>");
		out.print("<tr><td>");
		out.print("<h4>Salary:</h4>");
		out.print("</td><td>");
		out.print("<input type='text' name='salary' value='" + salary + "'/>");
		out.print("</td></tr>");
		out.print("<tr><td>");
		out.print("<input type='submit' value='Save'/>");
		out.print("</td><td>");
		out.print("<a href='ViewEmployee'>Cancel</a>");
		out.print("</td></tr>");
		out.print("</table>");
		out.print("</form>");
	}
}
