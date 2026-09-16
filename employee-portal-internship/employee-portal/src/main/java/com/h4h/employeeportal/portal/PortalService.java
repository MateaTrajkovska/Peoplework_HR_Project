package com.h4h.employeeportal.portal;

import com.h4h.employeeportal.absence.core.enumeration.*;
import com.h4h.employeeportal.absence.core.model.*;
import com.h4h.employeeportal.absence.core.service.AbsenceService;
import com.h4h.employeeportal.employee.core.enumeration.*;
import com.h4h.employeeportal.employee.core.model.*;
import com.h4h.employeeportal.portal.PortalDtos.*;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import jakarta.persistence.*;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PortalService {

 @PersistenceContext
 private EntityManager em;

 private final AbsenceService absenceService;

 public PortalService(AbsenceService absenceService) {
  this.absenceService = absenceService;
 }

 public <T> List<T> all(Class<T> c) {
  return em.createQuery("from " + c.getSimpleName(), c).getResultList();
 }

 public <T> T get(Class<T> c, UUID id) {
  T v = em.find(c, id);
  if (v == null) {
   throw new ResourceNotFoundException("Record not found.");
  }
  return v;
 }

 private <T> T part(Class<T> c, UUID id) {
  return em
          .createQuery("from " + c.getSimpleName() + " where employeeUuid=:id", c)
          .setParameter("id", id)
          .getResultStream()
          .findFirst()
          .orElse(null);
 }

 private WorkProfile profile(UUID id) {
  return em
          .createQuery("from WorkProfile where employeeId=:id", WorkProfile.class)
          .setParameter("id", id)
          .getResultStream()
          .findFirst()
          .orElse(null);
 }

 private void check(boolean valid, String msg) {
  if (!valid) {
   throw new IllegalArgumentException(msg);
  }
 }

 private Employee active(UUID id) {
  Employee e = get(Employee.class, id);
  em.lock(e, LockModeType.PESSIMISTIC_WRITE);
  em.flush();
  em.refresh(e);
  check(e.getStatus() == EmployeeStatusEnum.ACTIVE, "This employee is archived.");
  return e;
 }

 public void log(String title, String detail, String kind, UUID id) {
  Activity a = new Activity();
  a.title = title;
  a.detail = detail;
  a.kind = kind;
  a.employeeId = id;
  em.persist(a);
 }

 public Map<String, Object> employee(Employee e) {
  Map<String, Object> v = new LinkedHashMap<>();
  UUID id = e.getUuid();
  v.put("id", id);
  v.put("firstName", e.getFirstName());
  v.put("lastName", e.getLastName());
  v.put("employeeNumber", e.getEmployeeNumber());
  v.put("status", e.getStatus());
  v.put("personalId", e.getPersonalId());
  v.put("nationality", e.getNationality());
  v.put("gender", e.getGender());

  EmployeeContact c = part(EmployeeContact.class, id);
  if (c != null) {
   v.put("email", c.getEmail());
   v.put("phone", c.getPhoneNumber());
  }

  EmployeeInfo i = part(EmployeeInfo.class, id);
  if (i != null) {
   v.put("dateOfBirth", i.getDateOfBirth());
   v.put("startDate", i.getStartDate());
   v.put("endDate", i.getEndDate());
   v.put("degree", i.getDegree());
   v.put("employeeType", i.getEmployeeType());
  }

  EmployeeAddress a = part(EmployeeAddress.class, id);
  if (a != null) {
   v.put("municipality", a.getMunicipality());
   v.put("street", a.getStreet());
   v.put("postalCode", a.getPostalCode());
  }

  EmployeeFinance f = part(EmployeeFinance.class, id);
  v.put("bankAccount", f == null ? "" : f.getBankAccount().toString());

  EmployeeReport r = part(EmployeeReport.class, id);
  v.put("notes", r == null ? "" : r.getInternalNote());

  WorkProfile p = profile(id);
  if (p != null) {
   v.put("departmentId", p.departmentId);
   v.put("department", p.departmentId == null ? "Unassigned" : get(Department.class, p.departmentId).name);
   v.put("jobTitle", p.jobTitle);
   v.put("location", p.location);
   v.put("workMode", p.workMode);
   v.put("avatarColor", p.avatarColor);
  }
  return v;
 }

 public List<Map<String, Object>> employees() {
  return all(Employee.class)
          .stream()
          .sorted(Comparator.comparing(Employee::getEmployeeNumber))
          .map(this::employee)
          .toList();
 }

 public Map<String, Object> saveEmployee(UUID id, EmployeeInput x) {
  check(x.dateOfBirth().isBefore(LocalDate.now()), "Date of birth must be in the past.");
  check(!x.startDate().isBefore(x.dateOfBirth().plusYears(16)), "Check the birth and employment dates.");
  get(Department.class, x.departmentId());

  final UUID existingId = id;
  boolean duplicate = all(EmployeeContact.class)
          .stream()
          .anyMatch(c -> c.getEmail().equalsIgnoreCase(x.email().trim()) && !c.getEmployeeUuid().equals(existingId));
  check(!duplicate, "This email already belongs to an employee.");

  Employee e = id == null ? new Employee() : active(id);
  e.setFirstName(x.firstName().trim());
  e.setLastName(x.lastName().trim());
  e.setPersonalId(x.personalId() == null || x.personalId().isBlank() ? null : x.personalId().trim());
  e.setNationality(x.nationality());
  e.setGender(EmployeeGenderEnum.valueOf(Objects.requireNonNullElse(x.gender(), "OTHER")));
  e.setStatus(EmployeeStatusEnum.ACTIVE);

  if (id == null) {
   em.persist(e);
   em.flush();
  }
  id = e.getUuid();

  EmployeeContact c = part(EmployeeContact.class, id);
  if (c == null) {
   c = new EmployeeContact();
   c.setEmployeeUuid(id);
  }
  c.setEmail(x.email().toLowerCase().trim());
  c.setPhoneNumber(x.phone());
  c.setContactType(EmployeeContactTypeEnum.CORPORATE);
  if (c.getUuid() == null) {
   em.persist(c);
  }

  EmployeeAddress a = part(EmployeeAddress.class, id);
  if (a == null) {
   a = new EmployeeAddress();
   a.setEmployeeUuid(id);
  }
  a.setMunicipality(x.municipality());
  a.setStreet(Objects.requireNonNullElse(x.street(), ""));
  a.setPostalCode(Objects.requireNonNullElse(x.postalCode(), ""));
  if (a.getUuid() == null) {
   em.persist(a);
  }

  EmployeeInfo i = part(EmployeeInfo.class, id);
  if (i == null) {
   i = new EmployeeInfo();
   i.setEmployeeUuid(id);
  }
  i.setDateOfBirth(x.dateOfBirth());
  i.setStartDate(x.startDate());
  i.setDegree(EmployeeDegreeEnum.valueOf(Objects.requireNonNullElse(x.degree(), "NONE")));
  i.setEmployeeType(EmployeeTypeEnum.valueOf(Objects.requireNonNullElse(x.employeeType(), "FULL_TIME")));
  i.setIdentificationNumber(Objects.requireNonNullElse(e.getPersonalId(), ""));
  i.setPosition(EmployeePositionEnum.HR);
  i.setPriorExperience(Period.ZERO);
  if (i.getUuid() == null) {
   em.persist(i);
  }

  EmployeeFinance f = part(EmployeeFinance.class, id);
  if (f == null) {
   f = new EmployeeFinance();
   f.setEmployeeUuid(id);
  }
  f.setBankAccount(x.bankAccount() == null || x.bankAccount().isBlank() ? 0L : Long.parseLong(x.bankAccount()));
  if (f.getUuid() == null) {
   em.persist(f);
  }

  EmployeeReport r = part(EmployeeReport.class, id);
  if (r == null) {
   r = new EmployeeReport();
   r.setEmployeeUuid(id);
  }
  r.setInternalNote(x.notes());
  if (r.getUuid() == null) {
   em.persist(r);
  }

  WorkProfile w = profile(id);
  if (w == null) {
   w = new WorkProfile();
   w.employeeId = id;
   w.avatarColor = List.of("purple", "blue", "green", "orange").get(Math.abs(id.hashCode() % 4));
   em.persist(w);
  }
  w.departmentId = x.departmentId();
  w.jobTitle = x.jobTitle();
  w.location = x.location();
  w.workMode = x.workMode();

  log("Employee details saved", e.getFirstName() + " " + e.getLastName(), "employee", id);
  em.flush();
  return employee(e);
 }

 public void terminate(UUID id, Termination x) {
  Employee e = active(id);
  EmployeeInfo i = part(EmployeeInfo.class, id);
  check(i == null || !x.date().isBefore(i.getStartDate()), "Termination cannot precede the employment start date.");
  check(!x.date().isAfter(LocalDate.now()), "Use today's date or a past date to archive immediately.");

  for (AbsenceRequest r : all(AbsenceRequest.class)) {
   if (get(Absence.class, r.getAbsenceUuid()).getEmployeeUuid().equals(id)
           && (r.getAbsenceRequestStatus() == AbsenceRequestStatusEnum.PENDING
           || (r.getAbsenceRequestStatus() == AbsenceRequestStatusEnum.APPROVED && !r.getFromDate().isBefore(x.date())))) {
    decide(r.getUuid(), "CANCELLED");
   }
  }

  for (Contract c : all(Contract.class)) {
   if (c.employeeId.equals(id) && !"TERMINATED".equals(c.status)) {
    c.status = "TERMINATED";
    if (c.endDate == null || c.endDate.isAfter(x.date())) {
     c.endDate = x.date();
    }
   }
  }

  e.setStatus(EmployeeStatusEnum.ARCHIVED);
  if (i != null) {
   i.setEndDate(x.date());
  }
  log("Employee archived", e.getFirstName() + " " + e.getLastName() + ": " + x.reason().substring(0, Math.min(150, x.reason().length())), "employee", id);
 }

 public Department saveDepartment(UUID id, DepartmentInput x) {
  Department d = id == null ? new Department() : get(Department.class, id);
  d.name = x.name().trim();
  d.description = x.description();
  if (id == null) {
   em.persist(d);
  }
  log("Department saved", d.name, "department", null);
  return d;
 }

 public void deleteDepartment(UUID id) {
  check(all(WorkProfile.class).stream().noneMatch(p -> id.equals(p.departmentId)), "Reassign the department's employees before deleting it.");
  em.remove(get(Department.class, id));
 }

 public Contract saveContract(UUID id, ContractInput x) {
  Employee e = active(x.employeeId());
  check(x.endDate() == null || !x.endDate().isBefore(x.startDate()), "End date must be on or after start date.");
  check(Set.of("PERMANENT", "FIXED_TERM", "INTERNSHIP", "PART_TIME").contains(x.type()), "Invalid contract type.");
  check(!"FIXED_TERM".equals(x.type()) || x.endDate() != null, "Fixed-term contracts require an end date.");
  check(Set.of("MKD", "EUR", "USD").contains(x.currency()), "Unsupported currency.");

  Contract c = id == null ? new Contract() : get(Contract.class, id);
  check(id == null || !"TERMINATED".equals(c.status), "Terminated contracts are read-only.");
  check(id == null || c.employeeId.equals(x.employeeId()), "A contract cannot be reassigned to another employee.");

  for (Contract other : all(Contract.class)) {
   if (other.employeeId.equals(x.employeeId()) && !other.id.equals(id) && !"TERMINATED".equals(other.status)) {
    check((other.endDate != null && other.endDate.isBefore(x.startDate())) || (x.endDate() != null && x.endDate().isBefore(other.startDate)), "This employee already has a contract covering these dates.");
   }
  }

  c.employeeId = x.employeeId();
  c.number = x.number().trim();
  c.type = x.type();
  c.startDate = x.startDate();
  c.endDate = x.endDate();
  c.salary = x.salary();
  c.currency = x.currency();
  c.notes = x.notes();
  c.status = "ACTIVE";
  if (id == null) {
   em.persist(c);
  }
  log("Contract saved", c.number + " · " + e.getFirstName() + " " + e.getLastName(), "contract", c.employeeId);
  return c;
 }

 public void endContract(UUID id) {
  Contract c = get(Contract.class, id);
  active(c.employeeId);
  check(!"TERMINATED".equals(c.status), "Contract is already terminated.");
  c.status = "TERMINATED";
  log("Contract terminated", c.number, "contract", c.employeeId);
 }

 public DocumentTemplate saveTemplate(UUID id, TemplateInput x) {
  DocumentTemplate t = id == null ? new DocumentTemplate() : get(DocumentTemplate.class, id);
  t.name = x.name();
  t.category = x.category();
  t.body = x.body();
  if (id == null) {
   em.persist(t);
  }
  return t;
 }

 public void deleteTemplate(UUID id) {
  em.remove(get(DocumentTemplate.class, id));
 }

 public Holiday saveHoliday(HolidayInput x) {
  Holiday h = new Holiday();
  h.date = x.date();
  h.name = x.name();
  em.persist(h);
  return h;
 }

 public void deleteHoliday(UUID id) {
  em.remove(get(Holiday.class, id));
 }

 public int days(LocalDate start, LocalDate end) {
  check(!end.isBefore(start), "End date must be on or after start date.");
  check(end.toEpochDay() - start.toEpochDay() < 367, "Choose a date range shorter than one year.");
  Set<LocalDate> holidays = new HashSet<>(all(Holiday.class).stream().map(h -> h.date).toList());
  return (int) start.datesUntil(end.plusDays(1))
          .filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY && !holidays.contains(d))
          .count();
 }

 private Absence plan(UUID id, int year) {
  return em.createQuery("from Absence where employeeUuid=:id and validFrom=:date", Absence.class)
          .setParameter("id", id)
          .setParameter("date", LocalDate.of(year, 1, 1))
          .getResultStream()
          .findFirst()
          .orElse(null);
 }

 private Absence ensurePlan(UUID id, int year) {
  Absence a = plan(id, year);
  return a == null ? absenceService.createAbsence(id, year) : a;
 }

 private int reserved(Absence a) {
  int total = 0;
  for (AbsenceRequest r : all(AbsenceRequest.class)) {
   if (r.getAbsenceRequestStatus() == AbsenceRequestStatusEnum.PENDING && r.getType() == AbsenceTypeEnum.VACATION) {
    Absence current = get(Absence.class, r.getAbsenceUuid());
    if (current.getUuid().equals(a.getUuid())) {
     total += Objects.requireNonNullElse(r.getDaysUsedFromCurrentYear(), 0);
    }
    if (current.getEmployeeUuid().equals(a.getEmployeeUuid()) && current.getYear() == a.getYear() + 1) {
     total += Objects.requireNonNullElse(r.getDaysUsedFromPreviousYear(), 0);
    }
   }
  }
  return total;
 }

 public List<Map<String, Object>> plans(int year) {
  return all(Absence.class)
          .stream()
          .filter(a -> a.getYear() == year)
          .map(a -> {
           Map<String, Object> m = new LinkedHashMap<>();
           m.put("id", a.getUuid());
           m.put("employeeId", a.getEmployeeUuid());
           m.put("year", a.getYear());
           m.put("maxDays", a.getMaxDays());
           m.put("usedDays", a.getUsedDays());
           m.put("reservedDays", reserved(a));
           m.put("remainingDays", a.getMaxDays() - a.getUsedDays() - reserved(a));
           m.put("validTo", a.getValidTo());
           return m;
          })
          .toList();
 }

 public void savePlan(PlanInput x) {
  active(x.employeeId());
  Absence a = ensurePlan(x.employeeId(), x.year());
  check(x.maxDays() >= a.getUsedDays() + reserved(a), "Allowance cannot be less than approved and reserved days.");
  a.setMaxDays(x.maxDays());
  log("Leave allowance updated", x.year() + " · " + x.maxDays() + " days", "leave", x.employeeId());
 }

 public void generatePlans(int year) {
  check(year >= 2020 && year <= 2100, "Invalid year.");
  for (Employee e : all(Employee.class)) {
   if (e.getStatus() == EmployeeStatusEnum.ACTIVE) {
    active(e.getUuid());
    ensurePlan(e.getUuid(), year);
   }
  }
 }

 public Map<String, Object> leave(AbsenceRequest r) {
  Map<String, Object> m = new LinkedHashMap<>();
  m.put("id", r.getUuid());
  m.put("employeeId", get(Absence.class, r.getAbsenceUuid()).getEmployeeUuid());
  m.put("number", r.getDecisionNumber());
  m.put("fromDate", r.getFromDate());
  m.put("toDate", r.getToDate());
  m.put("days", Objects.requireNonNullElse(r.getDaysUsedFromPreviousYear(), 0) + Objects.requireNonNullElse(r.getDaysUsedFromCurrentYear(), 0));
  m.put("type", r.getType());
  m.put("status", r.getAbsenceRequestStatus());
  m.put("reason", r.getReason());
  m.put("createdOn", r.getCreatedOn());
  return m;
 }

 public List<Map<String, Object>> leaves() {
  return all(AbsenceRequest.class)
          .stream()
          .sorted(Comparator.comparing(AbsenceRequest::getCreatedOn).reversed())
          .map(this::leave)
          .toList();
 }

 public Map<String, Object> requestLeave(LeaveInput x) {
  Employee e = active(x.employeeId());
  check(x.fromDate().getYear() == x.toDate().getYear(), "Submit separate requests for different calendar years.");
  EmployeeInfo info = part(EmployeeInfo.class, x.employeeId());
  check(info == null || !x.fromDate().isBefore(info.getStartDate()), "Leave cannot precede employment.");
  int count = days(x.fromDate(), x.toDate());
  check(count > 0, "The request must include at least one working day.");

  AbsenceTypeEnum type = AbsenceTypeEnum.valueOf(x.type());

  for (AbsenceRequest r : all(AbsenceRequest.class)) {
   if (get(Absence.class, r.getAbsenceUuid()).getEmployeeUuid().equals(x.employeeId())
           && (r.getAbsenceRequestStatus() == AbsenceRequestStatusEnum.PENDING || r.getAbsenceRequestStatus() == AbsenceRequestStatusEnum.APPROVED)) {
    check(r.getToDate().isBefore(x.fromDate()) || r.getFromDate().isAfter(x.toDate()), "This request overlaps an existing pending or approved absence.");
   }
  }

  Absence a = ensurePlan(x.employeeId(), x.fromDate().getYear());
  int previous = 0;

  if (type == AbsenceTypeEnum.VACATION) {
   Absence prev = plan(x.employeeId(), a.getYear() - 1);
   if (prev != null && !x.toDate().isAfter(prev.getValidTo())) {
    previous = Math.min(count, Math.max(0, prev.getMaxDays() - prev.getUsedDays() - reserved(prev)));
   }
   check(count - previous <= a.getMaxDays() - a.getUsedDays() - reserved(a), "Not enough available vacation days.");
  }

  AbsenceRequest r = new AbsenceRequest();
  r.setAbsenceUuid(a.getUuid());
  r.setDecisionDate(LocalDate.now());
  r.setDecisionNumber("LV-" + a.getYear() + "-" + em.createNativeQuery("select nextval('seq_absence_decision_number')").getSingleResult());
  r.setFromDate(x.fromDate());
  r.setToDate(x.toDate());
  r.setType(type);
  r.setReason(x.reason());
  r.setAbsenceRequestStatus(AbsenceRequestStatusEnum.PENDING);
  r.setDaysUsedFromPreviousYear(previous);
  r.setDaysUsedFromCurrentYear(count - previous);
  em.persist(r);

  log("Leave request submitted", e.getFirstName() + " " + e.getLastName() + " · " + count + " days", "leave", e.getUuid());
  return leave(r);
 }

 public Map<String, Object> decide(UUID id, String value) {
  AbsenceRequest r = get(AbsenceRequest.class, id);
  Absence a = get(Absence.class, r.getAbsenceUuid());
  active(a.getEmployeeUuid());
  em.flush();
  em.refresh(r);
  em.refresh(a);

  AbsenceRequestStatusEnum to = AbsenceRequestStatusEnum.valueOf(value);
  AbsenceRequestStatusEnum from = r.getAbsenceRequestStatus();

  check(to == AbsenceRequestStatusEnum.APPROVED || to == AbsenceRequestStatusEnum.DENIED || to == AbsenceRequestStatusEnum.CANCELLED, "Invalid decision.");
  check(from == AbsenceRequestStatusEnum.PENDING || (from == AbsenceRequestStatusEnum.APPROVED && to == AbsenceRequestStatusEnum.CANCELLED), "This request has already been processed.");

  if (r.getType() == AbsenceTypeEnum.VACATION) {
   int sign = to == AbsenceRequestStatusEnum.APPROVED ? 1 : from == AbsenceRequestStatusEnum.APPROVED ? -1 : 0;
   if (sign != 0) {
    a.setUsedDays(a.getUsedDays() + sign * r.getDaysUsedFromCurrentYear());
    check(a.getUsedDays() >= 0 && a.getUsedDays() <= a.getMaxDays(), "Invalid vacation balance.");
    if (r.getDaysUsedFromPreviousYear() > 0) {
     Absence prev = plan(a.getEmployeeUuid(), a.getYear() - 1);
     check(prev != null, "Previous-year plan is missing.");
     prev.setUsedDays(prev.getUsedDays() + sign * r.getDaysUsedFromPreviousYear());
     check(prev.getUsedDays() >= 0 && prev.getUsedDays() <= prev.getMaxDays(), "Invalid carryover balance.");
    }
   }
  }

  r.setAbsenceRequestStatus(to);
  log("Leave request " + value.toLowerCase(), r.getDecisionNumber(), "leave", a.getEmployeeUuid());
  return leave(r);
 }

 public Map<String, Object> dashboard() {
  LocalDate today = LocalDate.now();
  Map<String, Object> m = new LinkedHashMap<>();
  m.put("employees", all(Employee.class).stream().filter(e -> e.getStatus() == EmployeeStatusEnum.ACTIVE).count());
  m.put("contracts", all(Contract.class).stream().filter(c -> "ACTIVE".equals(c.status) && !c.startDate.isAfter(today) && (c.endDate == null || !c.endDate.isBefore(today))).count());
  m.put("pending", all(AbsenceRequest.class).stream().filter(r -> r.getAbsenceRequestStatus() == AbsenceRequestStatusEnum.PENDING).count());
  m.put("away", all(AbsenceRequest.class).stream().filter(r -> r.getAbsenceRequestStatus() == AbsenceRequestStatusEnum.APPROVED && !r.getFromDate().isAfter(today) && !r.getToDate().isBefore(today)).count());
  m.put("activities", all(Activity.class).stream().sorted(Comparator.comparing((Activity a) -> a.occurredAt).reversed()).limit(8).toList());
  return m;
 }
}