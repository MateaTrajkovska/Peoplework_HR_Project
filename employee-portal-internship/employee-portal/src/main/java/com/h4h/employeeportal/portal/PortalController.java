package com.h4h.employeeportal.portal;

import com.h4h.employeeportal.employee.core.model.Employee;
import com.h4h.employeeportal.portal.PortalDtos.*;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PortalController {

 private final PortalService s;

 public PortalController(PortalService s) {
  this.s = s;
 }

 @GetMapping("/dashboard")
 public Object dashboard() {
  return s.dashboard();
 }

 @GetMapping("/employees")
 public Object employees() {
  return s.employees();
 }

 @GetMapping("/employees/{id}")
 public Object employee(@PathVariable UUID id) {
  return s.employee(s.get(Employee.class, id));
 }

 @PostMapping("/employees")
 public Object create(@Valid @RequestBody EmployeeInput x) {
  return s.saveEmployee(null, x);
 }

 @PutMapping("/employees/{id}")
 public Object update(@PathVariable UUID id, @Valid @RequestBody EmployeeInput x) {
  return s.saveEmployee(id, x);
 }

 @PostMapping("/employees/{id}/terminate")
 public void terminate(@PathVariable UUID id, @Valid @RequestBody Termination x) {
  s.terminate(id, x);
 }

 @GetMapping("/departments")
 public Object departments() {
  return s.all(Department.class);
 }

 @PostMapping("/departments")
 public Object department(@Valid @RequestBody DepartmentInput x) {
  return s.saveDepartment(null, x);
 }

 @PutMapping("/departments/{id}")
 public Object department(@PathVariable UUID id, @Valid @RequestBody DepartmentInput x) {
  return s.saveDepartment(id, x);
 }

 @DeleteMapping("/departments/{id}")
 public void deleteDepartment(@PathVariable UUID id) {
  s.deleteDepartment(id);
 }

 @GetMapping("/contracts")
 public Object contracts() {
  return s.all(Contract.class);
 }

 @PostMapping("/contracts")
 public Object contract(@Valid @RequestBody ContractInput x) {
  return s.saveContract(null, x);
 }

 @PutMapping("/contracts/{id}")
 public Object contract(@PathVariable UUID id, @Valid @RequestBody ContractInput x) {
  return s.saveContract(id, x);
 }

 @PostMapping("/contracts/{id}/terminate")
 public void endContract(@PathVariable UUID id) {
  s.endContract(id);
 }

 @GetMapping("/leaves")
 public Object leaves() {
  return s.leaves();
 }

 @PostMapping("/leaves")
 public Object leave(@Valid @RequestBody LeaveInput x) {
  return s.requestLeave(x);
 }

 @PostMapping("/leaves/{id}/decision")
 public Object decision(@PathVariable UUID id, @Valid @RequestBody Decision x) {
  return s.decide(id, x.status());
 }

 @GetMapping("/plans")
 public Object plans(@RequestParam int year) {
  return s.plans(year);
 }

 @PostMapping("/plans")
 public void plan(@Valid @RequestBody PlanInput x) {
  s.savePlan(x);
 }

 @PostMapping("/plans/generate")
 public void generate(@RequestParam int year) {
  s.generatePlans(year);
 }

 @GetMapping("/templates")
 public Object templates() {
  return s.all(DocumentTemplate.class);
 }

 @PostMapping("/templates")
 public Object template(@Valid @RequestBody TemplateInput x) {
  return s.saveTemplate(null, x);
 }

 @PutMapping("/templates/{id}")
 public Object template(@PathVariable UUID id, @Valid @RequestBody TemplateInput x) {
  return s.saveTemplate(id, x);
 }

 @DeleteMapping("/templates/{id}")
 public void deleteTemplate(@PathVariable UUID id) {
  s.deleteTemplate(id);
 }

 @GetMapping("/holidays")
 public Object holidays() {
  return s.all(Holiday.class);
 }

 @PostMapping("/holidays")
 public Object holiday(@Valid @RequestBody HolidayInput x) {
  return s.saveHoliday(x);
 }

 @DeleteMapping("/holidays/{id}")
 public void deleteHoliday(@PathVariable UUID id) {
  s.deleteHoliday(id);
 }
}