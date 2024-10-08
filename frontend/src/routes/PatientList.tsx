import {Patient} from "../models/patient/Patient.ts";
import usePatientStore from "../stores/usePatientStore.ts";
import {ListHeader, PatientListContainer, PatientListItem} from "../styles/PatientList.styled.ts";
import PatientCard from "../components/patient/PatientCard.tsx";
import {useEffect} from "react";
import Typography from "../styles/Typography.tsx";
import {NumberEntry} from "../styles/PatientCard.styled.ts";
import useGlobalStore from "../stores/useGloblaStore.ts";

export default function PatientList() {
    const searchTerm = useGlobalStore(state => state.searchTerm).trim().toLowerCase();
    const fetchPatients: () => void = usePatientStore(state => state.fetchPatients);
    const patientList: Patient[] = usePatientStore(state => state.patients)
        .sort((a, b) => a.lastname.localeCompare(b.lastname));

    useEffect(() => {
        fetchPatients();
    }, []);

    const filteredPatients = searchTerm
        ? patientList.filter(patient =>
            patient.lastname.toLowerCase().includes(searchTerm))
        : patientList;

    return (
        <PatientListContainer>
            <ListHeader>
                <NumberEntry>Nr</NumberEntry>
                <Typography variant="h3">Name</Typography>
                <Typography variant="h3">Birthdate</Typography>
                <Typography variant="h3">{""}</Typography>
                <div></div>
            </ListHeader>
            {filteredPatients.map((patient, index) => (
                <PatientListItem key={patient.id}>
                    <PatientCard patient={patient} detailed={false} listNr={index + 1}/>
                </PatientListItem>

            ))}
        </PatientListContainer>
    )
}