package com.h4h.employeeportal.portal;

import com.h4h.employeeportal.employee.core.model.Employee;
import com.h4h.employeeportal.portal.PortalDtos.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "portal.seed-demo", havingValue = "true")
public class DemoData implements ApplicationRunner {

 private final PortalService s;

 public DemoData(PortalService s) {
  this.s = s;
 }

 @Override
 @Transactional
 public void run(ApplicationArguments args) {
  if (!s.all(Employee.class).isEmpty() || !s.all(Department.class).isEmpty()) return;

  List<Department> departments = List.of(
          s.saveDepartment(null, new DepartmentInput("Engineering", "Building thoughtful, reliable products.")),
          s.saveDepartment(null, new DepartmentInput("People & Culture", "A great place to do your best work.")),
          s.saveDepartment(null, new DepartmentInput("Design", "Human-centered experiences, down to the details.")),
          s.saveDepartment(null, new DepartmentInput("Operations", "Keeping our everyday work moving.")),
          s.saveDepartment(null, new DepartmentInput("Marketing", "Connecting our work with the world."))
  );

  String[][] people = {
          {"Matea", "Trajkovska", "HR Manager", "1"},
          {"Aleksandar", "Petrovski", "Senior Backend Engineer", "0"},
          {"Elena", "Stojanova", "Product Designer", "2"},
          {"Marko", "Nikolovski", "Frontend Engineer", "0"},
          {"Sara", "Ristovska", "People Partner", "1"},
          {"Stefan", "Kostovski", "QA Engineer", "0"},
          {"Ana", "Dimitrova", "Operations Lead", "3"},
          {"David", "Mitrevski", "Brand Strategist", "4"},
          {"Jana", "Petrova", "UX Researcher", "2"},
          {"Filip", "Ivanovski", "Software Engineer", "0"},
          {"Simona", "Todorova", "Marketing Specialist", "4"},
          {"Nikola", "Andonov", "Finance Specialist", "3"}
  };

  List<UUID> ids = new ArrayList<>();
  LocalDate now = LocalDate.now();

  for (int n = 0; n < people.length; n++) {
   String[] a = people[n];
   LocalDate start = now.minusYears(n % 4 + 1).withMonth((n % 8) + 1).withDayOfMonth(1);

   Map<String, Object> e = s.saveEmployee(
           null,
           new EmployeeInput(
                   a[0],
                   a[1],
                   a[0].toLowerCase() + "." + a[1].toLowerCase() + "@example.com",
                   "+389 70 000 " + String.format("%03d", n),
                   null,
                   "Macedonian",
                   LocalDate.of(1990 + n % 9, 2 + n % 8, 10),
                   start,
                   n % 2 == 0 ? "FEMALE" : "MALE",
                   "FULL_TIME",
                   n % 3 == 0 ? "MASTERS" : "BACHELORS",
                   a[2],
                   departments.get(Integer.parseInt(a[3])).id,
                   "Skopje",
                   "Demo office address",
                   "1000",
                   "Skopje, North Macedonia",
                   n % 3 == 0 ? "Remote" : "Hybrid",
                   "",
                   "Fictional demonstration record."
           )
   );

   UUID id = (UUID) e.get("id");
   ids.add(id);

   s.saveContract(
           null,
           new ContractInput(
                   id,
                   "EMP-" + now.getYear() + "-" + String.format("%03d", n + 1),
                   n % 4 == 0 ? "FIXED_TERM" : "PERMANENT",
                   start,
                   n % 4 == 0 ? now.plusDays(18 + n * 2) : null,
                   new BigDecimal(60000 + n * 4500),
                   "MKD",
                   "Demonstration employment record. Review with HR before use."
           )
   );

   s.savePlan(new PlanInput(id, now.getYear(), 24));
  }

  LocalDate from = now.plusDays(7);
  while (from.getDayOfWeek() != DayOfWeek.MONDAY) {
   from = from.plusDays(1);
  }

  if (from.plusDays(20).getYear() == now.getYear()) {
   s.requestLeave(new LeaveInput(ids.get(2), from, from.plusDays(4), "VACATION", "A little time to recharge."));
   s.requestLeave(new LeaveInput(ids.get(3), from.plusDays(7), from.plusDays(9), "VACATION", "Family trip."));
   s.requestLeave(new LeaveInput(ids.get(6), from, from.plusDays(1), "PAID_LEAVE", "Personal commitment."));

   Map<String, Object> leave = s.requestLeave(
           new LeaveInput(ids.get(5), from.plusDays(14), from.plusDays(18), "VACATION", "Planned annual leave.")
   );
   s.decide((UUID) leave.get("id"), "APPROVED");
  }

  s.saveTemplate(
          null,
          new TemplateInput(
                  "Employment confirmation",
                  "Employment",
                  "EMPLOYMENT CONFIRMATION\n\nIssued on {{today}}\n\nThis confirms that {{fullName}}, employee number {{employeeNumber}}, is employed as {{jobTitle}} in {{department}}.\n\nEmployment start date: {{startDate}}\n\nPrepared by Human Resources.\n\nAuthorized signature: __________________\n\nDraft — review before issuing."
          )
  );

  s.saveTemplate(
          null,
          new TemplateInput(
                  "Leave decision",
                  "Leave",
                  "LEAVE DECISION\n\nEmployee: {{fullName}}\nDepartment: {{department}}\nDate issued: {{today}}\n\nDecision number: __________\nApproved dates: __________\nWorking days: __________\n\nHR signature: __________________\n\nDraft — complete the dates and review before issuing."
          )
  );

  s.saveTemplate(
          null,
          new TemplateInput(
                  "Onboarding checklist",
                  "Onboarding",
                  "WELCOME, {{fullName}}\n\nYour role: {{jobTitle}}\nYour team: {{department}}\nStart date: {{startDate}}\n\n[ ] Complete employee information\n[ ] Review employment contract\n[ ] Prepare equipment and accounts\n[ ] Introduce your team\n[ ] Schedule the first-week check-in\n\nPrepared on {{today}}"
          )
  );
 }
}