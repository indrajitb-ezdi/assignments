package employeeportal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(
		name = "DeleteEmployee",
		urlPatterns = {"/DeleteEmployee"},
		loadOnStartup=1
)
public class DeleteEmployee extends HttpServlet {
	private EmployeeDAO empDAO;
	
	public DeleteEmployee() {
		empDAO = new EmployeeDAO();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		if(request.getParameterMap().containsKey("id")) {
			Integer empId = Integer.parseInt(request.getParameter("id"));
			Employee emp = empDAO.getEmployee(empId);
			if(request.getParameterMap().containsKey("confirmed")) {
				// Perform delete
				boolean succeded = empDAO.deleteEmployee(empId);
				if(succeded) {
					out.println("<h2>Deleted successfully ...</h2>");
					out.println("<a href='ViewEmployee'>Continue</a>");
				}
				else {
					out.println("<h2>Deletion failed ...</h2>");
					out.println("<a href='ViewEmployee'>Continue</a>");
				}
			}
			else {
				// Ask for confirmation
				displayConfirmForm(out, Integer.toString(empId), emp.getFirstName(), emp.getLastName(), Integer.toString(emp.getSalary()));
			}
		}
		else {
			// Invalid call
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Invalid page request!");
		}
		out.close();
	}
	
	private void displayConfirmForm(PrintWriter out, String id, String firstName, String lastName, String salary) {
		out.println("<h2>Confirm Employee Deletion</h2>");
		out.print("<form action='DeleteEmployee' method='post'>");
		out.print("<input type='hidden' name='id' value='" + id + "' />");
		out.print("<input type='hidden' name='confirmed' />");
		out.print("<table border='0'");
		out.print("<tr><td>");
		out.print("<h4>First Name:</h4>");
		out.print("</td><td>");
		out.print("<h4>" + firstName + "</h4>");
		out.print("</td></tr>");
		out.print("<tr><td>");
		out.print("<h4>Last Name:</h4>");
		out.print("</td><td>");
		out.print("<h4>" + lastName + "</h4>");
		out.print("</td></tr>");
		out.print("<tr><td>");
		out.print("<h4>Salary:</h4>");
		out.print("</td><td>");
		out.print("<h4>" + salary + "</h4>");
		out.print("</td></tr>");
		out.print("<tr><td>");
		out.print("<input type='submit' value='Delete'/>");
		out.print("</td><td>");
		out.print("<a href='ViewEmployee'>Cancel</a>");
		out.print("</td></tr>");
		out.print("</table>");
		out.print("</form>");
	}
}
