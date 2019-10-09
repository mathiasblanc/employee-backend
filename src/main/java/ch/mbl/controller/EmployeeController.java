package ch.mbl.controller;

import ch.mbl.model.Employee;
import ch.mbl.exception.ResourceNotFoundException;
import ch.mbl.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Employee persisted = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could " +
                "not find employee"));

        return ResponseEntity.ok().body(persisted);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long id,
                                                   @Valid @RequestBody Employee employee) throws ResourceNotFoundException {
        Employee persisted = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could " +
                "not find employee"));

        persisted.setFirstName(employee.getFirstName());
        persisted.setLastName(employee.getLastName());
        persisted.setEmail(employee.getEmail());
        employeeRepository.save(persisted);

        return ResponseEntity.ok().body(persisted);
    }

    @DeleteMapping("/employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Employee persisted = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(("Could " +
                "not find employee")));
        employeeRepository.delete(persisted);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}