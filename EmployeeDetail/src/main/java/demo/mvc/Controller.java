package demo.mvc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/")
public class Controller {
	@Autowired
	private EmployeeDAO employeeDAO;
	
	@GetMapping("/employees")
//	@RequestMapping(method = RequestMethod.GET, value="/employees")
	public List<Employee> getEmployees() {
		return employeeDAO.listEmployees();
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployee(@PathVariable("id") Integer id) {
		Employee employee = employeeDAO.getEmployee(id);
		if(employee == null)
			return new ResponseEntity<Employee>(new Employee(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	@PostMapping("/employees")
	public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {
		if(employee.getFirstName() == null)
			employee.setFirstName("");
		if(employee.getLastName() == null)
			employee.setLastName("");
		
		Integer id = employeeDAO.addEmployee(employee.getFirstName(), employee.getLastName(), employee.getSalary());
		if(id == null)
			return new ResponseEntity<String>("Employee detail could not be created", HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<String>("Employee detail created with id = " + id, HttpStatus.CREATED);
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<String> updateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
		if(employee.getFirstName() == null)
			employee.setFirstName("");
		if(employee.getLastName() == null)
			employee.setLastName("");
		
		if(employeeDAO.updateEmployee(id, employee.getFirstName(), employee.getLastName(), employee.getSalary()))
			return new ResponseEntity<String>("Employee detail updated for id = " + id, HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<String>("Employee detail could not be updated for id = " + id, 
					HttpStatus.INTERNAL_SERVER_ERROR);	
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") Integer id) {
		if(employeeDAO.deleteEmployee(id))
			return new ResponseEntity<String>("Employee detail successfully deleted for id = " + id, 
					HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<String>("Employee detail could not be deleted for id = " + id, 
					HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
