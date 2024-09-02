export type HealthData = {
    id: string,
    gender: string,
    ageAtFirstAdmissionDate: Date,
    medicalExaminations: MedicalExamination[],
}

export type HealthDataToAdd = {
    gender: string,
    ageAtFirstAdmissionDate: Date,
    medicalExaminations: MedicalExamination[],
}

type MedicalExamination = {
    examinationDate: Date,
    icdCodes: IcdCode[],
    symptoms: string,
    diagnosis: string,
    medications: string[],
    treatments: Treatment[],
    vitalSigns: VitalSigns[],
    additionalNotes: string,
}

type IcdCode = {
    code: string,
    description: string,
}

type Treatment = {
    type: string,
    description: string
}

type VitalSigns = {
    temperature: number,
    heartRate: number,
    bloodPressureSystolic: number,
    bloodPressureDiastolic: number,
    respiratoryRate: number
}