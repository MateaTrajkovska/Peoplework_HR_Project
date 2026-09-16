package com.h4h.employeeportal.absence.restapi.api;

import com.h4h.employeeportal.absence.restapi.dto.AbsenceRequestDto;
import com.h4h.employeeportal.absence.restapi.dto.create.CreateAbsenceRequestDto;
import com.h4h.employeeportal.absence.restapi.dto.update.UpdateAbsenceRequestDto;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.absence.restapi.restservice.AbsenceRequestRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("core/absence-request")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AbsenceRequestRestController {

    private final AbsenceRequestRestService absenceRequestRestService;

    @PostMapping
    public AbsenceRequestDto createAbsenceRequest(@Valid @RequestBody CreateAbsenceRequestDto createAbsenceRequestDto) {
        return absenceRequestRestService.createAbsenceRequest(createAbsenceRequestDto);
    }

    @GetMapping
    public List<AbsenceRequestDto> getAllAbsenceRequests() {
        return absenceRequestRestService.getAllAbsenceRequests();
    }

    @GetMapping("/{id}")
    public AbsenceRequestDto getAbsenceRequestById(@PathVariable UUID id) {
        return absenceRequestRestService.getAbsenceRequestById(id);
    }

    @PutMapping("/{id}")
    public AbsenceRequestDto updateAbsenceRequest(@PathVariable UUID id, @Valid @RequestBody UpdateAbsenceRequestDto updateAbsenceRequestDto) {
        return absenceRequestRestService.updateAbsenceRequest(id, updateAbsenceRequestDto);
    }

    @DeleteMapping("/{id}")
    public DeleteDto deleteAbsenceRequest(@PathVariable UUID id) {
        return absenceRequestRestService.deleteAbsenceRequest(id);
    }

    @PatchMapping("/{id}/approve")
    public AbsenceRequestDto approveAbsenceRequest(@PathVariable UUID id) {
        return absenceRequestRestService.approveAbsenceRequest(id);
    }

    @PatchMapping("/{id}/deny")
    public AbsenceRequestDto denyAbsenceRequest(@PathVariable UUID id) {
        return absenceRequestRestService.denyAbsenceRequest(id);
    }
}