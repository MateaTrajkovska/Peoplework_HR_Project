package com.h4h.employeeportal.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

 @GetMapping({
         "/",
         "/employees",
         "/employees/{id}/detail",
         "/departments",
         "/contracts",
         "/leaves",
         "/plans",
         "/calendar",
         "/templates",
         "/settings"
 })
 public String workspace() {
  return "forward:/index.html";
 }
}