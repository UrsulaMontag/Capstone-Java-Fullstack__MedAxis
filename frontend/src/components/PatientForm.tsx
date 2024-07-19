import usePatientStore from "../stores/usePatientStore.ts";
import {PatientToAdd} from "../models/Patient.ts";
import {FormEvent, useState} from "react";

export default function PatientForm() {
    const createPatient: (newPatient: PatientToAdd) => void = usePatientStore((state) => state.createPatient);
    const initialInputState = {
        firstname: "",
        lastname: "",
        dateOfBirth: ""
    };
    const [patientInput, setPatientInput] = useState<PatientToAdd>(initialInputState);

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        createPatient(patientInput);
        setPatientInput(initialInputState);
    }

    return (
        <form onSubmit={(event: FormEvent<HTMLFormElement>) => handleSubmit(event)}>

        </form>
    )
}