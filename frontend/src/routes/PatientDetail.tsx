import PatientCard from "../components/patient/PatientCard.tsx";
import {Patient} from "../models/patient/Patient.ts";
import usePatientStore from "../stores/usePatientStore.ts";
import {Params, useParams} from "react-router-dom";


export default function PatientDetail() {
    const patients: Patient[] = usePatientStore(state => state.patients);

    const urlParams: Readonly<Params<string>> = useParams();
    const currentPatient: Patient | undefined = patients.find(patient => patient.id === urlParams.id);
    const currentProtectedPatient: Patient | undefined = patients.find(patient => patient.healthDataId === urlParams.healthDataId)


    return (
        <>{currentPatient
            ? <PatientCard patient={currentPatient} detailed={true}/>
            : currentProtectedPatient
                ? <PatientCard patient={currentProtectedPatient} detailed={true}/>
                : <p>Patient not found!</p>}
        </>

    )
}