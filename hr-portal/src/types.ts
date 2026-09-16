export interface Employee {
    id: string;
    firstName: string;
    lastName: string;
    employeeNumber: number;
    status: string;
    email: string;
    phone: string;
    personalId: string;
    nationality: string;
    gender: string;
    dateOfBirth: string;
    startDate: string;
    endDate?: string;
    degree: string;
    employeeType: string;
    departmentId: string;
    department: string;
    jobTitle: string;
    municipality: string;
    street: string;
    postalCode: string;
    location: string;
    workMode: string;
    avatarColor: string;
    bankAccount: string;
    notes: string;
}

export interface Department {
    id: string;
    name: string;
    description: string;
}

export interface Contract {
    id: string;
    employeeId: string;
    number: string;
    type: string;
    status: string;
    startDate: string;
    endDate: string;
    salary: number;
    currency: string;
    notes: string;
}

export interface Leave {
    id: string;
    employeeId: string;
    number: string;
    fromDate: string;
    toDate: string;
    days: number;
    type: string;
    status: string;
    reason: string;
}

export interface Plan {
    id: string;
    employeeId: string;
    year: number;
    maxDays: number;
    usedDays: number;
    reservedDays: number;
    remainingDays: number;
    validTo: string;
}

export interface Template {
    id: string;
    name: string;
    category: string;
    body: string;
}

export interface Holiday {
    id: string;
    date: string;
    name: string;
}

export interface Activity {
    id: string;
    title: string;
    detail: string;
    kind: string;
    employeeId: string;
    occurredAt: string;
}

export interface DashboardData {
    employees: number;
    contracts: number;
    pending: number;
    away: number;
    activities: Activity[];
}