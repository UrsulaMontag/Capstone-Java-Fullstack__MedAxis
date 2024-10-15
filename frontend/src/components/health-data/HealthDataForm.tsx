import {Patient} from "../../models/patient/Patient.ts";
import usePatientStore from "../../stores/usePatientStore.ts";
import {FormEvent, useState} from "react";
import {StyledForm, FormGroup, FormLabel, FormInput, FormTextarea, ErrorInfo} from "../../styles/Form.styled.tsx";
import Typography from "../../styles/Typography.tsx";
import {CardContainer} from "../../styles/PatientCard.styled.ts";
import {MedicalExamination, Treatment} from "../../models/healt-data/HealthData.ts";
import useHealthDataStore from "../../stores/useHealthDataStore.ts";
import IcdECTWrapper from "../icd_api/IcdECTWrapper.tsx";

type HealthDataFormProps = {
    patientId: string;
    onComplete: () => void;
};

export default function HealthDataForm({patientId, onComplete}: Readonly<HealthDataFormProps>) {
    const patients: Patient[] = usePatientStore(state => state.patients);
    const updatePatientsHealthData = useHealthDataStore(state => state.addExaminationWithIcdCodes);

    const currentPatient: Patient | undefined = patients.find(patient => patient.id === patientId);

    const initialExaminationState: MedicalExamination = {
        examinationDate: new Date().toISOString(),
        icdCodes: [],
        symptoms: "",
        diagnosis: "",
        medications: [],
        treatments: [],
        vitalSigns: [{
            temperature: 37.0, // Default temperature in Celsius
            heartRate: 70, // Default heart rate in bpm
            bloodPressureSystolic: 120, // Default systolic blood pressure
            bloodPressureDiastolic: 80, // Default diastolic blood pressure
            respiratoryRate: 16 // Default respiratory rate in breaths/min
        }],
        additionalNotes: ""
    };

    const [examinationInput, setExaminationInput] = useState<MedicalExamination>(initialExaminationState);
    const [isIcdInputClicked, setIsIcdInputClicked] = useState(false);
    const [newMedication, setNewMedication] = useState("");
    const [newTreatment, setNewTreatment] = useState<Treatment>({type: "", description: ""});
    const [errors, setErrors] = useState<{ [key: string]: string }>({});

    const validateForm = () => {
        const newErrors: { [key: string]: string } = {};
        if (!examinationInput.symptoms.trim()) {
            newErrors.symptoms = "Symptoms are required.";
        }
        if (examinationInput.treatments.length === 0) {
            newErrors.treatments = "At least one treatment is required.";
        }
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleIcdInputClick = () => {
        setIsIcdInputClicked(true);
    };

    const handleIcdCodeSelection = (icdCode: { code: string; description: string }) => {
        setExaminationInput(prev => ({
            ...prev,
            icdCodes: [...prev.icdCodes, icdCode]
        }));
    };

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!validateForm()) {
            alert("Please fill out all required fields.");
            return
        }
        if (currentPatient) {
            try {
                const formattedExamination = {
                    ...examinationInput,
                    examinationDate: new Date(examinationInput.examinationDate).toISOString()
                };
                updatePatientsHealthData(currentPatient.healthDataId, formattedExamination);
                onComplete();
            } catch (error) {
                console.error("Error updating health data:", error);
                alert("Failed to update health data. Please try again.");
            }
        }
        alert("Select patient first.");
        return;
    };

    const handleCancel = () => {
        onComplete();
    };

    const handleAddMedication = () => {
        if (newMedication) {
            setExaminationInput(prev => ({
                ...prev,
                medications: [...prev.medications, newMedication]
            }));
            setNewMedication("");
        }
    };

    const handleAddTreatment = () => {
        if (newTreatment.type && newTreatment.description) {
            setExaminationInput(prev => ({
                ...prev,
                treatments: [...prev.treatments, newTreatment]
            }));
            setNewTreatment({type: "", description: ""});
        }
    };

    return (
        <>
            {currentPatient && (
                <CardContainer details={true}>
                    <StyledForm onSubmit={handleSubmit}>
                        <FormGroup>
                            <FormLabel>Symptoms:</FormLabel>
                            <FormTextarea
                                value={examinationInput.symptoms}
                                onChange={(e) => setExaminationInput({...examinationInput, symptoms: e.target.value})}
                                placeholder="Symptoms"
                            />
                            {errors.symptoms && <ErrorInfo>{errors.symptoms}</ErrorInfo>}

                        </FormGroup>
                        <FormGroup>
                            <FormLabel>Diagnosis:</FormLabel>
                            <FormInput
                                type="text"
                                value={examinationInput.diagnosis}
                                onChange={(e) => setExaminationInput({...examinationInput, diagnosis: e.target.value})}
                                placeholder="Diagnosis"
                            />
                        </FormGroup>
                        <Typography variant="h4">Medications</Typography>
                        <ul>
                            {examinationInput.medications.map((med, index) => (
                                <li key={index}>{med}</li>
                            ))}
                        </ul>
                        <input
                            type="text"
                            value={newMedication}
                            onChange={(e) => setNewMedication(e.target.value)}
                            placeholder="New medication"
                        />
                        <button type="button" onClick={handleAddMedication}>Add Medication</button>

                        <Typography variant="h4">Treatments</Typography>
                        <ul>
                            {examinationInput.treatments.map((treatment, index) => (
                                <li key={index}>{treatment.type}: {treatment.description}</li>
                            ))}
                        </ul>
                        <input
                            type="text"
                            value={newTreatment.type}
                            onChange={(e) => setNewTreatment({...newTreatment, type: e.target.value})}
                            placeholder="Treatment type"
                        />
                        <input
                            type="text"
                            value={newTreatment.description}
                            onChange={(e) => setNewTreatment({...newTreatment, description: e.target.value})}
                            placeholder="Treatment description"
                        />
                        <button type="button" onClick={handleAddTreatment}>Add Treatment</button>
                        {errors.treatments && <ErrorInfo>{errors.treatments}</ErrorInfo>}

                        <Typography variant="h4">Vital Signs</Typography>
                        <input
                            type="number"
                            value={examinationInput.vitalSigns[0].temperature}
                            onChange={(e) => setExaminationInput({
                                ...examinationInput,
                                vitalSigns: [{
                                    ...examinationInput.vitalSigns[0],
                                    temperature: parseFloat(e.target.value)
                                }]
                            })}
                            placeholder="Temperature"
                        />
                        <input
                            type="number"
                            value={examinationInput.vitalSigns[0].heartRate}
                            onChange={(e) => setExaminationInput({
                                ...examinationInput,
                                vitalSigns: [{...examinationInput.vitalSigns[0], heartRate: parseInt(e.target.value)}]
                            })}
                            placeholder="Heart Rate"
                        />
                        <input
                            type="number"
                            value={examinationInput.vitalSigns[0].bloodPressureSystolic}
                            onChange={(e) => setExaminationInput({
                                ...examinationInput,
                                vitalSigns: [{
                                    ...examinationInput.vitalSigns[0],
                                    bloodPressureSystolic: parseInt(e.target.value)
                                }]
                            })}
                            placeholder="Blood Pressure Systolic"
                        />
                        <input
                            type="number"
                            value={examinationInput.vitalSigns[0].bloodPressureDiastolic}
                            onChange={(e) => setExaminationInput({
                                ...examinationInput,
                                vitalSigns: [{
                                    ...examinationInput.vitalSigns[0],
                                    bloodPressureDiastolic: parseInt(e.target.value)
                                }]
                            })}
                            placeholder="Blood Pressure Diastolic"
                        />
                        <input
                            type="number"
                            value={examinationInput.vitalSigns[0].respiratoryRate}
                            onChange={(e) => setExaminationInput({
                                ...examinationInput,
                                vitalSigns: [{
                                    ...examinationInput.vitalSigns[0],
                                    respiratoryRate: parseInt(e.target.value)
                                }]
                            })}
                            placeholder="Respiratory Rate"
                        />

                        <Typography variant="h4">ICD Codes</Typography>
                        <ul>
                            {examinationInput.icdCodes.map((icd, index) => (
                                <li key={index}>{icd.code}: {icd.description}</li>
                            ))}
                        </ul>
                        {!isIcdInputClicked && (
                            <input type="text" onClick={handleIcdInputClick} readOnly
                                   placeholder="Click to add ICD code"/>
                        )}
                        {isIcdInputClicked && (
                            <IcdECTWrapper onSelect={handleIcdCodeSelection}/>
                        )}

                        <textarea
                            value={examinationInput.additionalNotes}
                            onChange={(e) => setExaminationInput({
                                ...examinationInput,
                                additionalNotes: e.target.value
                            })}
                            placeholder="Additional Notes"
                        />

                        <button type="submit">Save</button>
                        <button type="button" onClick={handleCancel}>Cancel</button>
                    </StyledForm>
                </CardContainer>
            )}
        </>
    );
}