import {Patient} from "../../models/patient/Patient.ts";
import {HealthData} from "../../models/healt-data/HealthData.ts";
import Typography from "../../styles/Typography.tsx";
import {formatDate} from "../../utils/formatDateView.ts";
import useHealthDataStore from "../../stores/useHealthDataStore.ts";
import {useState} from "react";
import {MedicalExamination} from "../../models/healt-data/HealthData.ts";
import ExaminationDetails from "../../models/healt-data/ExaminationDetails.tsx";
import HealthDataForm from "./HealthDataForm.tsx";


type PatientHealthCardProps = {
    patient: Patient;
}

export default function PatientHealthCard(props: Readonly<PatientHealthCardProps>) {
    const healthData: HealthData = useHealthDataStore(state => state.healthData);
    const getHealthDataById = useHealthDataStore(state => state.getHealthDataById);

    const [selectedExamination, setSelectedExamination] = useState<MedicalExamination | null>(null);
    const [isAddingNewExamination, setIsAddingNewExamination] = useState(false);

    const sortedExaminations = [...healthData.medicalExaminations].sort((a, b) => new Date(b.examinationDate).getTime() - new Date(a.examinationDate).getTime());
    const latestExamination = sortedExaminations[0];

    const handleAddNewExamination = () => {
        setIsAddingNewExamination(true);
    };

    const handleFormComplete = () => {
        setIsAddingNewExamination(false);
        getHealthDataById(props.patient.healthDataId); // Fetch updated health data
    };

    const handleExaminationClick = (examination: MedicalExamination) => {
        setSelectedExamination(examination);
    };

    return (

        <div>
            <Typography variant="h2">Patient Health Card</Typography>
            <Typography variant="h3">Name: {props.patient.lastname} {props.patient.firstname}</Typography>
            <Typography variant="h3">Birthdate: {formatDate(props.patient.dateOfBirth)}</Typography>

            {latestExamination && (
                <div>
                    <Typography variant="h3">Latest Examination</Typography>
                    <ExaminationDetails examination={latestExamination}/>
                </div>
            )}

            <button onClick={handleAddNewExamination}>Add New Examination</button>

            {isAddingNewExamination ? (
                <HealthDataForm
                    patientId={props.patient.id}
                    onComplete={handleFormComplete}
                />
            ) : (
                <div>
                    <Typography variant="h3">Examination History</Typography>
                    <ul>
                        {sortedExaminations.map((examination) => (
                            <li key={examination.examinationDate} onClick={() => handleExaminationClick(examination)}
                                onKeyDown={() => handleExaminationClick(examination)}>
                                {formatDate(examination.examinationDate.toString())} - {examination.diagnosis}
                            </li>
                        ))}
                    </ul>
                </div>
            )}

            {selectedExamination && (
                <ExaminationDetails examination={selectedExamination}/>
            )}
        </div>
    );
}