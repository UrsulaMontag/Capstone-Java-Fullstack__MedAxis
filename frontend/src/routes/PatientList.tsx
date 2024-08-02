import {Patient} from "../models/Patient.ts";
import usePatientStore from "../stores/usePatientStore.ts";
import {ListHeader, PatientListContainer, PatientListItem} from "../styles/PatientList.styled.ts";
import PatientCard from "../components/PatientCard.tsx";
import {useEffect} from "react";
import Typography from "../styles/Typography.tsx";
import {NumberEntry} from "../styles/PatientCard.styled.ts";

export default function PatientList() {
    const fetchPatients: () => void = usePatientStore(state => state.fetchPatients);
    const patientList: Patient[] = usePatientStore(state => state.patients);

    useEffect(() => {
        fetchPatients();
    }, [])

    return (
        <PatientListContainer>
            <ListHeader>
                <NumberEntry>Nr</NumberEntry>
                <Typography variant="h3">Name</Typography>
                <Typography variant="h3">Birthdate</Typography>
                <Typography variant="h3">{""}</Typography>
                <div></div>
            </ListHeader>
            {patientList.map((patient, index) => (
                <PatientListItem key={patient.id}>
                    <PatientCard patient={patient} detailed={false} listNr={index + 1}/>
                </PatientListItem>

            ))}
        </PatientListContainer>
    )
}