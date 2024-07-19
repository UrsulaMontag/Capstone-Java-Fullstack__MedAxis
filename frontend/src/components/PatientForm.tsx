import usePatientStore from "../stores/usePatientStore.ts";
import {PatientToAdd} from "../models/Patient.ts";
import {ChangeEvent, FormEvent, useState} from "react";

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
        alert("")
    }

    return (
        <form onSubmit={(event: FormEvent<HTMLFormElement>) => handleSubmit(event)}>
            <label>Firstname: </label>
            <input type="text" value={patientInput.firstname} onChange={(event: ChangeEvent<HTMLInputElement>) => {
                setPatientInput({...patientInput, firstname: event.target.value});
            }}/>
            <label>Lastname: </label>
            <input type="text" value={patientInput.lastname} onChange={(event: ChangeEvent<HTMLInputElement>) => {
                setPatientInput({...patientInput, lastname: event.target.value});
            }}/>
            <label>Date of Birth: </label>
            <input type="text" value={patientInput.dateOfBirth} onChange={(event: ChangeEvent<HTMLInputElement>) => {
                setPatientInput({...patientInput, dateOfBirth: event.target.value});
            }}/>
            <button>Register</button>
            <button>Cancel</button>
        </form>
    )
}