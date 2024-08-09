import {ContactInformation} from "./ContactInformation.ts";

export type Patient = {
    id: string;
    firstname: string;
    lastname: string;
    dateOfBirth: string;
    insuranceNr: string;
    contactInformation: ContactInformation;
    icdCodes?: string[];
}

export type PatientToAdd = {
    firstname: string;
    lastname: string;
    dateOfBirth: string;
    insuranceNr: string;
    contactInformation: ContactInformation;
}
