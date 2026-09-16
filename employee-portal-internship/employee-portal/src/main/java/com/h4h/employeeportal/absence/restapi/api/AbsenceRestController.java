package com.h4h.employeeportal.absence.restapi.api;

import com.h4h.employeeportal.absence.restapi.dto.AbsenceDto;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.absence.restapi.dto.create.CreateAbsenceDto;
import com.h4h.employeeportal.absence.restapi.dto.update.UpdateAbsenceDto;
import com.h4h.employeeportal.absence.restapi.restservice.AbsenceRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("core/absence")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AbsenceRestController {

    private final AbsenceRestService absenceRestService;

    @PostMapping
    public AbsenceDto createAbsence(@Valid @RequestBody CreateAbsenceDto createAbsenceDto) {
        return absenceRestService.createAbsence(createAbsenceDto);
    }

    @GetMapping
    public List<AbsenceDto> getAllAbsences() {
        return absenceRestService.getAllAbsences();
    }

    @GetMapping("/{id}")
    public AbsenceDto getAbsenceById(@PathVariable UUID id) {
        return absenceRestService.getAbsenceById(id);
    }

    @PutMapping("/{id}")
    public AbsenceDto updateAbsenceStatus(@PathVariable UUID id, @Valid @RequestBody UpdateAbsenceDto updateAbsenceDto) {
        return absenceRestService.updateAbsenceStatus(id, updateAbsenceDto);
    }

    @DeleteMapping("/{id}")
    public DeleteDto deleteAbsence(@PathVariable UUID id) {
        return absenceRestService.deleteAbsence(id);
    }
}