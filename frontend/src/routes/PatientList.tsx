import {Patient} from "../models/Patient.ts";
import usePatientStore from "../stores/usePatientStore.ts";
import {PatientListContainer, PatientListItem} from "../styles/PatientList.styled.ts";
import PatientCard from "../components/PatientCard.tsx";
import {useEffect} from "react";

export default function PatientList() {
    const fetchPatients: () => void = usePatientStore(state => state.fetchPatients);
    useEffect(() => {
        fetchPatients();
    }, [])
    const patientList: Patient[] = usePatientStore(state => state.patients).sort();

    return (
        <PatientListContainer>
            {patientList.map((patient) => (
                <PatientListItem>
                    <PatientCard key={patient.id} patient={patient}/>
                </PatientListItem>

            ))}
        </PatientListContainer>
    )
}