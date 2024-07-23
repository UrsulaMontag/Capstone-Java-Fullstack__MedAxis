import PatientCard from "../components/PatientCard.tsx";
import {Patient} from "../models/Patient.ts";
import usePatientStore from "../stores/usePatientStore.ts";
import {Params, useParams} from "react-router-dom";

export default function PatientDetail() {
    const patients: Patient[] = usePatientStore(state => state.patients);
    const urlParams: Readonly<Params<string>> = useParams();
    const currentPatient: Patient | undefined = patients.find(patient => patient.id === urlParams.id);
    return (
        <>{currentPatient
            ? <PatientCard patient={currentPatient} detailed={true}/>
            : <p>Patient not found!</p>}
        </>

    )
}