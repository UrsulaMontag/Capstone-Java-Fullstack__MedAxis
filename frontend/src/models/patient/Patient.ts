import {ContactInformation, EmergencyContact} from "./ContactInformation.ts";

export type Patient = {
    id: string;
    firstname: string;
    lastname: string;
    dateOfBirth: string;
    gender: string;
    emergencyContact: EmergencyContact;
    nationality: string;
    maritalStatus: string;
    primaryLanguage: string;
    occupation: string;
    insuranceNr: string;
    contactInformation: ContactInformation;
    healthDataId: string;
}

export type PatientToAdd = {
    firstname: string;
    lastname: string;
    dateOfBirth: string;
    gender: string;
    emergencyContact: EmergencyContact;
    nationality: string;
    maritalStatus: string;
    primaryLanguage: string;
    occupation: string;
    insuranceNr: string;
    contactInformation: ContactInformation;
}
