export type HealthData = {
    id: string,
    gender: string,
    ageAtFirstAdmissionDate: number,
    medicalExaminations: MedicalExamination[],
}

export type HealthDataToAdd = {
    gender: string,
    ageAtFirstAdmissionDate: number,
    medicalExaminations: MedicalExamination[],
}

export type MedicalExamination = {
    examinationDate: string,
    icdCodes: IcdCode[],
    symptoms: string,
    diagnosis: string,
    medications: string[],
    treatments: Treatment[],
    vitalSigns: VitalSigns[],
    additionalNotes: string,
}

export type IcdCode = {
    code: string,
    description: string,
}

export type Treatment = {
    type: string,
    description: string
}

export type VitalSigns = {
    temperature: number,
    heartRate: number,
    bloodPressureSystolic: number,
    bloodPressureDiastolic: number,
    respiratoryRate: number
}