package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactTypeEnum;
import com.h4h.employeeportal.employee.core.model.*;
import com.h4h.employeeportal.employee.core.service.*;
import com.h4h.employeeportal.employee.restapi.dto.*;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.mapper.*;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeRestServiceTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeContactService employeeContactService;

    @Mock
    private EmployeeAddressService employeeAddressService;

    @Mock
    private EmployeeFinanceService employeeFinanceService;

    @Mock
    private EmployeeInfoService employeeInfoService;

    @Mock
    private EmployeeReportService employeeReportService;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeContactMapper employeeContactMapper;

    @Mock
    private EmployeeAddressMapper employeeAddressMapper;

    @Mock
    private EmployeeFinanceMapper employeeFinanceMapper;

    @Mock
    private EmployeeInfoMapper employeeInfoMapper;

    @Mock
    private EmployeeReportMapper employeeReportMapper;

    @InjectMocks
    private EmployeeRestService employeeRestService;

    private UUID employeeUuid;

    @BeforeEach
    void setUp() {
        employeeUuid = UUID.randomUUID();
    }

    @Test
    void createEmployee_shouldCreateEmployeeAndAllRelatedEntities() {
        CreateEmployeeDto createDto = new CreateEmployeeDto();

        CreateEmployeeContactDto contactDto =
                mockCreateEmployeeContactDto("john@company.com", "071111111", EmployeeContactTypeEnum.CORPORATE);

        CreateEmployeeContactDto secondContactDto =
                mockCreateEmployeeContactDto("john@gmail.com", "072222222", EmployeeContactTypeEnum.PERSONAL);

        createDto.setEmployeeContacts(List.of(contactDto, secondContactDto));

        Employee employeeToSave = new Employee();
        Employee savedEmployee = mockEmployee();

        EmployeeContact contact = new EmployeeContact();
        EmployeeContact secondContact = new EmployeeContact();

        EmployeeAddress address = new EmployeeAddress();
        EmployeeFinance finance = new EmployeeFinance();
        EmployeeInfo info = new EmployeeInfo();
        EmployeeReport report = new EmployeeReport();

        EmployeeDto expectedDto = new EmployeeDto();
        expectedDto.setId(employeeUuid);

        when(employeeMapper.toEntityCreate(createDto)).thenReturn(employeeToSave);
        when(employeeService.createEmployee(employeeToSave)).thenReturn(savedEmployee);

        when(employeeContactMapper.toEntityCreate(contactDto)).thenReturn(contact);
        when(employeeContactMapper.toEntityCreate(secondContactDto)).thenReturn(secondContact);

        when(employeeAddressMapper.toEntityCreateFromEmployee(createDto)).thenReturn(address);
        when(employeeFinanceMapper.toEntityCreateFromEmployee(createDto)).thenReturn(finance);
        when(employeeInfoMapper.toEntityCreateFromEmployee(createDto)).thenReturn(info);
        when(employeeReportMapper.toEntityCreateFromEmployee(createDto)).thenReturn(report);

        when(employeeMapper.toDto(savedEmployee)).thenReturn(expectedDto);

        EmployeeDto result = employeeRestService.createEmployee(createDto);

        assertSame(expectedDto, result);

        verify(employeeMapper).toEntityCreate(createDto);
        verify(employeeService).createEmployee(employeeToSave);

        verify(employeeContactMapper).toEntityCreate(contactDto);
        verify(employeeContactMapper).toEntityCreate(secondContactDto);

        verify(employeeContactService).createEmployeeContact(contact);
        verify(employeeContactService).createEmployeeContact(secondContact);

        verify(employeeAddressMapper).toEntityCreateFromEmployee(createDto);
        verify(employeeFinanceMapper).toEntityCreateFromEmployee(createDto);
        verify(employeeInfoMapper).toEntityCreateFromEmployee(createDto);
        verify(employeeReportMapper).toEntityCreateFromEmployee(createDto);

        verify(employeeAddressService).createEmployeeAddress(address);
        verify(employeeFinanceService).createEmployeeFinance(finance);
        verify(employeeInfoService).createEmployeeInfo(info);
        verify(employeeReportService).createEmployeeReport(report);

        assertAll(
                () -> assertEquals(employeeUuid, contact.getEmployeeUuid()),
                () -> assertEquals(employeeUuid, secondContact.getEmployeeUuid()),
                () -> assertEquals(employeeUuid, address.getEmployeeUuid()),
                () -> assertEquals(employeeUuid, finance.getEmployeeUuid()),
                () -> assertEquals(employeeUuid, info.getEmployeeUuid()),
                () -> assertEquals(employeeUuid, report.getEmployeeUuid()));

        verify(employeeMapper).toDto(savedEmployee);
    }

    @Test
    void createEmployee_shouldPropagateEmployeeServiceException() {
        CreateEmployeeDto createDto = new CreateEmployeeDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.notFound");

        when(employeeMapper.toEntityCreate(createDto)).thenReturn(new Employee());

        when(employeeService.createEmployee(any(Employee.class))).thenThrow(exception);

        assertSame(
                exception,
                assertThrows(ResourceNotFoundException.class, () -> employeeRestService.createEmployee(createDto)));

        verifyNoInteractions(employeeContactService);
        verifyNoInteractions(employeeAddressService);
        verifyNoInteractions(employeeFinanceService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void createEmployee_shouldStopWhenContactCreationFails() {
        CreateEmployeeDto createDto = new CreateEmployeeDto();

        CreateEmployeeContactDto firstDto = mockCreateEmployeeContactDto(null, null, null);

        CreateEmployeeContactDto secondDto = mockCreateEmployeeContactDto(null, null, null);

        createDto.setEmployeeContacts(List.of(firstDto, secondDto));

        Employee savedEmployee = mockEmployee();

        EmployeeContact firstContact = new EmployeeContact();
        EmployeeContact secondContact = new EmployeeContact();

        when(employeeMapper.toEntityCreate(createDto)).thenReturn(new Employee());

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(savedEmployee);

        when(employeeContactMapper.toEntityCreate(firstDto)).thenReturn(firstContact);

        when(employeeContactMapper.toEntityCreate(secondDto)).thenReturn(secondContact);

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.contact.notFound");

        doThrow(exception).when(employeeContactService).createEmployeeContact(any(EmployeeContact.class));

        assertSame(
                exception,
                assertThrows(ResourceNotFoundException.class, () -> employeeRestService.createEmployee(createDto)));

        verify(employeeContactService, times(1)).createEmployeeContact(any(EmployeeContact.class));

        verifyNoInteractions(employeeAddressService);
        verifyNoInteractions(employeeFinanceService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void createEmployee_shouldPropagateAddressCreationException() {
        CreateEmployeeDto createDto = new CreateEmployeeDto();

        createDto.setEmployeeContacts(List.of(mockCreateEmployeeContactDto(null, null, null)));

        Employee savedEmployee = mockEmployee();

        EmployeeContact contact = new EmployeeContact();
        EmployeeAddress address = new EmployeeAddress();
        EmployeeFinance finance = new EmployeeFinance();
        EmployeeInfo info = new EmployeeInfo();
        EmployeeReport report = new EmployeeReport();

        when(employeeMapper.toEntityCreate(createDto)).thenReturn(new Employee());

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(savedEmployee);

        when(employeeContactMapper.toEntityCreate(any(CreateEmployeeContactDto.class)))
                .thenReturn(contact);

        when(employeeAddressMapper.toEntityCreateFromEmployee(createDto)).thenReturn(address);

        when(employeeFinanceMapper.toEntityCreateFromEmployee(createDto)).thenReturn(finance);

        when(employeeInfoMapper.toEntityCreateFromEmployee(createDto)).thenReturn(info);

        when(employeeReportMapper.toEntityCreateFromEmployee(createDto)).thenReturn(report);

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.address.notFound");

        doThrow(exception).when(employeeAddressService).createEmployeeAddress(address);

        assertSame(
                exception,
                assertThrows(ResourceNotFoundException.class, () -> employeeRestService.createEmployee(createDto)));

        verify(employeeAddressService).createEmployeeAddress(address);

        verifyNoInteractions(employeeFinanceService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void createEmployee_shouldThrowExceptionWhenContactsAreNull() {
        CreateEmployeeDto createDto = new CreateEmployeeDto();
        createDto.setEmployeeContacts(null);

        Employee savedEmployee = mockEmployee();

        when(employeeMapper.toEntityCreate(createDto)).thenReturn(new Employee());

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(savedEmployee);

        assertThrows(NullPointerException.class, () -> employeeRestService.createEmployee(createDto));

        verify(employeeService).createEmployee(any(Employee.class));

        verifyNoInteractions(employeeContactService);
        verifyNoInteractions(employeeAddressService);
        verifyNoInteractions(employeeFinanceService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void getAllEmployees_shouldReturnEmptyListWhenNoEmployeesExist() {
        when(employeeService.getAllEmployees()).thenReturn(List.of());
        when(employeeMapper.toDtoList(List.of())).thenReturn(List.of());

        List<EmployeeListDto> result = employeeRestService.getAllEmployees();

        assertAll(() -> assertNotNull(result), () -> assertTrue(result.isEmpty()));

        verify(employeeService).getAllEmployees();
        verify(employeeMapper).toDtoList(List.of());

        verifyNoInteractions(employeeContactService);
        verifyNoInteractions(employeeAddressService);
    }

    @Test
    void getAllEmployees_shouldUseCorporateEmailWhenAvailable() {
        Employee employee = mockEmployee();

        EmployeeListDto employeeDto = new EmployeeListDto();

        EmployeeContact corporateContact = new EmployeeContact();

        EmployeeContactDto corporateDto = new EmployeeContactDto();
        corporateDto.setEmail("corporate@test.com");
        corporateDto.setContactType(EmployeeContactTypeEnum.CORPORATE);

        EmployeeAddress address = new EmployeeAddress();

        EmployeeAddressDto addressDto = new EmployeeAddressDto();
        addressDto.setMunicipality("Centar");

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        when(employeeMapper.toDtoList(List.of(employee))).thenReturn(List.of(employeeDto));

        when(employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenReturn(List.of(corporateContact));

        when(employeeContactMapper.toDtoList(List.of(corporateContact))).thenReturn(List.of(corporateDto));

        when(employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenReturn(address);

        when(employeeAddressMapper.toDto(address)).thenReturn(addressDto);

        List<EmployeeListDto> result = employeeRestService.getAllEmployees();

        assertAll(
                () -> assertEquals("corporate@test.com", result.getFirst().getEmail()),
                () -> assertEquals("Centar", result.getFirst().getMunicipality()));
    }

    @Test
    void getAllEmployees_shouldUsePersonalEmailWhenCorporateDoesNotExist() {
        Employee employee = mockEmployee();

        EmployeeListDto employeeDto = new EmployeeListDto();

        EmployeeContact personalContact = new EmployeeContact();

        EmployeeContactDto personalDto = new EmployeeContactDto();
        personalDto.setEmail("personal@test.com");
        personalDto.setContactType(EmployeeContactTypeEnum.PERSONAL);

        EmployeeAddress address = new EmployeeAddress();
        EmployeeAddressDto addressDto = new EmployeeAddressDto();

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        when(employeeMapper.toDtoList(List.of(employee))).thenReturn(List.of(employeeDto));

        when(employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenReturn(List.of(personalContact));

        when(employeeContactMapper.toDtoList(List.of(personalContact))).thenReturn(List.of(personalDto));

        when(employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenReturn(address);

        when(employeeAddressMapper.toDto(address)).thenReturn(addressDto);

        List<EmployeeListDto> result = employeeRestService.getAllEmployees();

        assertEquals("personal@test.com", result.getFirst().getEmail());
    }

    @Test
    void getAllEmployees_shouldPreferCorporateOverPersonalEmail() {
        Employee employee = mockEmployee();

        EmployeeListDto employeeDto = new EmployeeListDto();

        EmployeeContact personal = new EmployeeContact();
        EmployeeContact corporate = new EmployeeContact();

        EmployeeContactDto personalDto = new EmployeeContactDto();
        personalDto.setEmail("personal@test.com");
        personalDto.setContactType(EmployeeContactTypeEnum.PERSONAL);

        EmployeeContactDto corporateDto = new EmployeeContactDto();
        corporateDto.setEmail("corporate@test.com");
        corporateDto.setContactType(EmployeeContactTypeEnum.CORPORATE);

        EmployeeAddress address = new EmployeeAddress();
        EmployeeAddressDto addressDto = new EmployeeAddressDto();

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        when(employeeMapper.toDtoList(List.of(employee))).thenReturn(List.of(employeeDto));

        when(employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenReturn(List.of(personal, corporate));

        when(employeeContactMapper.toDtoList(List.of(personal, corporate)))
                .thenReturn(List.of(personalDto, corporateDto));

        when(employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenReturn(address);

        when(employeeAddressMapper.toDto(address)).thenReturn(addressDto);

        List<EmployeeListDto> result = employeeRestService.getAllEmployees();

        assertEquals("corporate@test.com", result.getFirst().getEmail());
    }

    @Test
    void getAllEmployees_shouldSetEmailToNullWhenNoSuitableContactExists() {
        Employee employee = mockEmployee();

        EmployeeListDto employeeDto = new EmployeeListDto();

        EmployeeContactDto otherContact = new EmployeeContactDto();
        otherContact.setEmail("other@test.com");

        EmployeeAddress address = new EmployeeAddress();
        EmployeeAddressDto addressDto = new EmployeeAddressDto();

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        when(employeeMapper.toDtoList(List.of(employee))).thenReturn(List.of(employeeDto));

        when(employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenReturn(List.of(new EmployeeContact()));

        when(employeeContactMapper.toDtoList(anyList())).thenReturn(List.of(otherContact));

        when(employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenReturn(address);

        when(employeeAddressMapper.toDto(address)).thenReturn(addressDto);

        List<EmployeeListDto> result = employeeRestService.getAllEmployees();

        assertNull(result.getFirst().getEmail());
    }

    @Test
    void getAllEmployees_shouldUseFirstCorporateEmailWhenMultipleCorporateContactsExist() {
        Employee employee = mockEmployee();

        EmployeeListDto employeeDto = new EmployeeListDto();

        EmployeeContactDto first = new EmployeeContactDto();
        first.setEmail("first@test.com");
        first.setContactType(EmployeeContactTypeEnum.CORPORATE);

        EmployeeContactDto second = new EmployeeContactDto();
        second.setEmail("second@test.com");
        second.setContactType(EmployeeContactTypeEnum.CORPORATE);

        EmployeeAddress address = new EmployeeAddress();
        EmployeeAddressDto addressDto = new EmployeeAddressDto();

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        when(employeeMapper.toDtoList(List.of(employee))).thenReturn(List.of(employeeDto));

        when(employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenReturn(List.of(new EmployeeContact(), new EmployeeContact()));

        when(employeeContactMapper.toDtoList(anyList())).thenReturn(List.of(first, second));

        when(employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenReturn(address);

        when(employeeAddressMapper.toDto(address)).thenReturn(addressDto);

        List<EmployeeListDto> result = employeeRestService.getAllEmployees();

        assertEquals("first@test.com", result.getFirst().getEmail());
    }

    @Test
    void getAllEmployees_shouldPropagateAddressNotFoundException() {
        Employee employee = mockEmployee();

        EmployeeListDto employeeDto = new EmployeeListDto();

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        when(employeeMapper.toDtoList(List.of(employee))).thenReturn(List.of(employeeDto));

        when(employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenReturn(List.of());

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.address.notFound");

        when(employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenThrow(exception);

        assertSame(
                exception, assertThrows(ResourceNotFoundException.class, () -> employeeRestService.getAllEmployees()));
    }

    @Test
    void getEmployeeById_shouldReturnCompleteEmployeeDetails() {
        Employee employee = mockEmployee();

        EmployeeDetailsDto expectedDto = new EmployeeDetailsDto();
        expectedDto.setId(employeeUuid);

        List<EmployeeContact> contacts = List.of(new EmployeeContact());
        List<EmployeeContactDto> contactDtos = List.of(new EmployeeContactDto());

        EmployeeAddress address = new EmployeeAddress();
        EmployeeAddressDto addressDto = new EmployeeAddressDto();

        EmployeeFinance finance = new EmployeeFinance();
        EmployeeFinanceDto financeDto = new EmployeeFinanceDto();

        EmployeeInfo info = new EmployeeInfo();
        EmployeeInfoDto infoDto = new EmployeeInfoDto();

        EmployeeReport report = new EmployeeReport();
        EmployeeReportDto reportDto = new EmployeeReportDto();

        when(employeeService.getEmployeeById(employeeUuid)).thenReturn(employee);

        when(employeeMapper.toDtoDetails(employee)).thenReturn(expectedDto);

        when(employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenReturn(contacts);

        when(employeeContactMapper.toDtoList(contacts)).thenReturn(contactDtos);

        when(employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenReturn(address);

        when(employeeAddressMapper.toDto(address)).thenReturn(addressDto);

        when(employeeFinanceService.getEmployeeFinanceByEmployeeUuid(employeeUuid))
                .thenReturn(finance);

        when(employeeFinanceMapper.toDto(finance)).thenReturn(financeDto);

        when(employeeInfoService.getEmployeeInfoByEmployeeUuid(employeeUuid)).thenReturn(info);

        when(employeeInfoMapper.toDto(info)).thenReturn(infoDto);

        when(employeeReportService.getEmployeeReportByEmployeeUuid(employeeUuid))
                .thenReturn(report);

        when(employeeReportMapper.toDto(report)).thenReturn(reportDto);

        EmployeeDetailsDto result = employeeRestService.getEmployeeById(employeeUuid);

        assertAll(
                () -> assertSame(expectedDto, result),
                () -> assertSame(contactDtos, result.getEmployeeContacts()),
                () -> assertSame(addressDto, result.getEmployeeAddress()),
                () -> assertSame(financeDto, result.getEmployeeFinance()),
                () -> assertSame(infoDto, result.getEmployeeInfo()),
                () -> assertSame(reportDto, result.getEmployeeReport()));
    }

    @Test
    void getEmployeeById_shouldPropagateEmployeeNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.notFound");

        when(employeeService.getEmployeeById(employeeUuid)).thenThrow(exception);

        assertSame(
                exception,
                assertThrows(ResourceNotFoundException.class, () -> employeeRestService.getEmployeeById(employeeUuid)));

        verifyNoInteractions(employeeMapper);
        verifyNoInteractions(employeeContactService);
        verifyNoInteractions(employeeAddressService);
        verifyNoInteractions(employeeFinanceService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void getEmployeeById_shouldPropagateContactException() {
        Employee employee = new Employee();

        when(employeeService.getEmployeeById(employeeUuid)).thenReturn(employee);

        when(employeeMapper.toDtoDetails(employee)).thenReturn(new EmployeeDetailsDto());

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.contact.notFound");

        when(employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenThrow(exception);

        assertSame(
                exception,
                assertThrows(ResourceNotFoundException.class, () -> employeeRestService.getEmployeeById(employeeUuid)));

        verifyNoInteractions(employeeAddressService);
        verifyNoInteractions(employeeFinanceService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void getEmployeeById_shouldPropagateAddressException() {
        Employee employee = new Employee();

        when(employeeService.getEmployeeById(employeeUuid)).thenReturn(employee);

        when(employeeMapper.toDtoDetails(employee)).thenReturn(new EmployeeDetailsDto());

        when(employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenReturn(List.of());

        when(employeeContactMapper.toDtoList(anyList())).thenReturn(List.of());

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.address.notFound");

        when(employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenThrow(exception);

        assertSame(
                exception,
                assertThrows(ResourceNotFoundException.class, () -> employeeRestService.getEmployeeById(employeeUuid)));

        verifyNoInteractions(employeeFinanceService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void updateEmployee_shouldUpdateEmployeeAndAllRelatedEntities() {
        UpdateEmployeeDto updateDto = new UpdateEmployeeDto();

        UpdateEmployeeContactDto contactDto = mockUpdateEmployeeContactDto();

        updateDto.setEmployeeContacts(List.of(contactDto));

        updateDto.setEmployeeFinance(new UpdateEmployeeFinanceDto());

        updateDto.setEmployeeAddress(new UpdateEmployeeAddressDto());

        updateDto.setEmployeeInfo(new UpdateEmployeeInfoDto());

        updateDto.setEmployeeReportDto(new UpdateEmployeeReportDto());

        Employee updatedEmployee = mockEmployee();

        EmployeeContact updatedContact = new EmployeeContact();

        EmployeeFinance existingFinance = new EmployeeFinance();
        existingFinance.setUuid(UUID.randomUUID());

        EmployeeFinance updatedFinance = new EmployeeFinance();

        EmployeeAddress existingAddress = new EmployeeAddress();
        existingAddress.setUuid(UUID.randomUUID());

        EmployeeAddress updatedAddress = new EmployeeAddress();

        EmployeeInfo existingInfo = new EmployeeInfo();
        existingInfo.setUuid(UUID.randomUUID());

        EmployeeInfo updatedInfo = new EmployeeInfo();

        EmployeeReport existingReport = new EmployeeReport();
        existingReport.setUuid(UUID.randomUUID());

        EmployeeReport updatedReport = new EmployeeReport();

        EmployeeDto expectedDto = new EmployeeDto();

        when(employeeMapper.toEntityUpdate(updateDto)).thenReturn(new Employee());

        when(employeeService.updateEmployee(eq(employeeUuid), any(Employee.class)))
                .thenReturn(updatedEmployee);

        when(employeeContactMapper.toEntityUpdate(contactDto)).thenReturn(updatedContact);

        when(employeeFinanceService.getEmployeeFinanceByEmployeeUuid(employeeUuid))
                .thenReturn(existingFinance);

        when(employeeFinanceMapper.toEntityUpdate(updateDto.getEmployeeFinance()))
                .thenReturn(updatedFinance);

        when(employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenReturn(existingAddress);

        when(employeeAddressMapper.toEntityUpdate(updateDto.getEmployeeAddress()))
                .thenReturn(updatedAddress);

        when(employeeInfoService.getEmployeeInfoByEmployeeUuid(employeeUuid)).thenReturn(existingInfo);

        when(employeeInfoMapper.toEntityUpdate(updateDto.getEmployeeInfo())).thenReturn(updatedInfo);

        when(employeeReportService.getEmployeeReportByEmployeeUuid(employeeUuid))
                .thenReturn(existingReport);

        when(employeeReportMapper.toEntityUpdate(updateDto.getEmployeeReportDto()))
                .thenReturn(updatedReport);

        when(employeeMapper.toDto(updatedEmployee)).thenReturn(expectedDto);

        EmployeeDto result = employeeRestService.updateEmployee(employeeUuid, updateDto);

        assertSame(expectedDto, result);

        verify(employeeService).updateEmployee(eq(employeeUuid), any(Employee.class));

        verify(employeeContactService).updateEmployeeContact(contactDto.getUuid(), updatedContact);

        verify(employeeFinanceService).updateEmployeeFinance(existingFinance.getUuid(), updatedFinance);

        verify(employeeAddressService).updateEmployeeAddress(existingAddress.getUuid(), updatedAddress);

        verify(employeeInfoService).updateEmployeeInfo(existingInfo.getUuid(), updatedInfo);

        verify(employeeReportService).updateEmployeeReport(existingReport.getUuid(), updatedReport);
    }

    @Test
    void updateEmployee_shouldUpdateMultipleContacts() {
        UpdateEmployeeDto updateDto = new UpdateEmployeeDto();

        UpdateEmployeeContactDto firstDto = mockUpdateEmployeeContactDto();

        UpdateEmployeeContactDto secondDto = mockUpdateEmployeeContactDto();

        updateDto.setEmployeeContacts(List.of(firstDto, secondDto));

        Employee updatedEmployee = mockEmployee();

        EmployeeContact firstContact = new EmployeeContact();
        EmployeeContact secondContact = new EmployeeContact();

        when(employeeMapper.toEntityUpdate(updateDto)).thenReturn(new Employee());

        when(employeeService.updateEmployee(eq(employeeUuid), any(Employee.class)))
                .thenReturn(updatedEmployee);

        when(employeeContactMapper.toEntityUpdate(firstDto)).thenReturn(firstContact);

        when(employeeContactMapper.toEntityUpdate(secondDto)).thenReturn(secondContact);

        when(employeeMapper.toDto(updatedEmployee)).thenReturn(new EmployeeDto());

        employeeRestService.updateEmployee(employeeUuid, updateDto);

        verify(employeeContactService).updateEmployeeContact(firstDto.getUuid(), firstContact);

        verify(employeeContactService).updateEmployeeContact(secondDto.getUuid(), secondContact);
    }

    @Test
    void updateEmployee_shouldSkipNullRelatedEntities() {
        UpdateEmployeeDto updateDto = new UpdateEmployeeDto();

        updateDto.setEmployeeContacts(List.of(mockUpdateEmployeeContactDto()));

        Employee updatedEmployee = mockEmployee();

        when(employeeMapper.toEntityUpdate(updateDto)).thenReturn(new Employee());

        when(employeeService.updateEmployee(eq(employeeUuid), any(Employee.class)))
                .thenReturn(updatedEmployee);

        when(employeeContactMapper.toEntityUpdate(any(UpdateEmployeeContactDto.class)))
                .thenReturn(new EmployeeContact());

        when(employeeMapper.toDto(updatedEmployee)).thenReturn(new EmployeeDto());

        employeeRestService.updateEmployee(employeeUuid, updateDto);

        verify(employeeService).updateEmployee(eq(employeeUuid), any(Employee.class));

        verify(employeeContactService).updateEmployeeContact(any(), any());

        verifyNoInteractions(employeeFinanceService);
        verifyNoInteractions(employeeAddressService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void updateEmployee_shouldPropagateEmployeeUpdateException() {
        UpdateEmployeeDto updateDto = new UpdateEmployeeDto();

        updateDto.setEmployeeContacts(List.of(mockUpdateEmployeeContactDto()));

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.notFound");

        when(employeeMapper.toEntityUpdate(updateDto)).thenReturn(new Employee());

        when(employeeService.updateEmployee(eq(employeeUuid), any(Employee.class)))
                .thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeRestService.updateEmployee(employeeUuid, updateDto)));

        verifyNoInteractions(employeeContactService);
        verifyNoInteractions(employeeFinanceService);
        verifyNoInteractions(employeeAddressService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void updateEmployee_shouldStopWhenContactUpdateFails() {
        UpdateEmployeeDto updateDto = new UpdateEmployeeDto();

        UpdateEmployeeContactDto firstDto = mockUpdateEmployeeContactDto();

        UpdateEmployeeContactDto secondDto = mockUpdateEmployeeContactDto();

        updateDto.setEmployeeContacts(List.of(firstDto, secondDto));

        Employee updatedEmployee = mockEmployee();

        EmployeeContact firstContact = new EmployeeContact();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.contact.notFound");

        when(employeeMapper.toEntityUpdate(updateDto)).thenReturn(new Employee());

        when(employeeService.updateEmployee(eq(employeeUuid), any(Employee.class)))
                .thenReturn(updatedEmployee);

        when(employeeContactMapper.toEntityUpdate(firstDto)).thenReturn(firstContact);

        doThrow(exception).when(employeeContactService).updateEmployeeContact(firstDto.getUuid(), firstContact);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeRestService.updateEmployee(employeeUuid, updateDto)));

        verify(employeeContactService).updateEmployeeContact(firstDto.getUuid(), firstContact);

        verify(employeeContactService, never())
                .updateEmployeeContact(eq(secondDto.getUuid()), any(EmployeeContact.class));

        verifyNoInteractions(employeeFinanceService);
        verifyNoInteractions(employeeAddressService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void updateEmployee_shouldPropagateFinanceUpdateException() {
        UpdateEmployeeDto updateDto = new UpdateEmployeeDto();

        updateDto.setEmployeeContacts(List.of(mockUpdateEmployeeContactDto()));

        updateDto.setEmployeeFinance(new UpdateEmployeeFinanceDto());

        Employee updatedEmployee = mockEmployee();

        EmployeeFinance existingFinance = new EmployeeFinance();
        existingFinance.setUuid(UUID.randomUUID());

        EmployeeFinance updatedFinance = new EmployeeFinance();

        when(employeeMapper.toEntityUpdate(updateDto)).thenReturn(new Employee());

        when(employeeService.updateEmployee(eq(employeeUuid), any(Employee.class)))
                .thenReturn(updatedEmployee);

        when(employeeContactMapper.toEntityUpdate(any(UpdateEmployeeContactDto.class)))
                .thenReturn(new EmployeeContact());

        when(employeeFinanceService.getEmployeeFinanceByEmployeeUuid(employeeUuid))
                .thenReturn(existingFinance);

        when(employeeFinanceMapper.toEntityUpdate(updateDto.getEmployeeFinance()))
                .thenReturn(updatedFinance);

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.finance.notFound");

        doThrow(exception)
                .when(employeeFinanceService)
                .updateEmployeeFinance(existingFinance.getUuid(), updatedFinance);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeRestService.updateEmployee(employeeUuid, updateDto)));

        verify(employeeFinanceService).updateEmployeeFinance(existingFinance.getUuid(), updatedFinance);

        verifyNoInteractions(employeeAddressService);
        verifyNoInteractions(employeeInfoService);
        verifyNoInteractions(employeeReportService);
    }

    @Test
    void deleteEmployee_shouldReturnDeleteDto() {
        DeleteDto expectedDto = new DeleteDto();

        when(employeeService.deleteEmployee(employeeUuid)).thenReturn(employeeUuid);

        when(employeeMapper.toDeletedDto(employeeUuid)).thenReturn(expectedDto);

        DeleteDto result = employeeRestService.deleteEmployee(employeeUuid);

        assertSame(expectedDto, result);

        verify(employeeService).deleteEmployee(employeeUuid);
        verify(employeeMapper).toDeletedDto(employeeUuid);
    }

    @Test
    void deleteEmployee_shouldPropagateException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.notFound");

        when(employeeService.deleteEmployee(employeeUuid)).thenThrow(exception);

        assertSame(
                exception,
                assertThrows(ResourceNotFoundException.class, () -> employeeRestService.deleteEmployee(employeeUuid)));

        verify(employeeService).deleteEmployee(employeeUuid);
        verify(employeeMapper, never()).toDeletedDto(any());
    }

    private Employee mockEmployee() {
        return Employee.builder().uuid(employeeUuid).build();
    }

    private CreateEmployeeContactDto mockCreateEmployeeContactDto(
            String email, String phoneNumber, EmployeeContactTypeEnum contactType) {

        return CreateEmployeeContactDto.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .contactType(contactType)
                .build();
    }

    private UpdateEmployeeContactDto mockUpdateEmployeeContactDto() {
        return UpdateEmployeeContactDto.builder().uuid(UUID.randomUUID()).build();
    }
}
