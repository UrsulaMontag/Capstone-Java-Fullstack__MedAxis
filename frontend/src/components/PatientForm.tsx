import usePatientStore from "../stores/usePatientStore.ts";
import {PatientToAdd} from "../models/Patient.ts";
import {ChangeEvent, FormEvent, useState} from "react";
import PatientFormStyled from "../styles/PatientForm.styled.tsx";
import Button from "../styles/Button.styled.tsx";

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
        //setPatientInput(initialInputState);
        alert(`New patient has been added successfully.`);
    }

    return (
        <PatientFormStyled onSubmit={(event: FormEvent<HTMLFormElement>) => handleSubmit(event)}>
            <label>Firstname: </label>
            <input required type="text" value={patientInput.firstname}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, firstname: event.target.value});
                   }}/>
            <label>Lastname: </label>
            <input required type="text" value={patientInput.lastname}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, lastname: event.target.value});
                   }}/>
            <label>Date of Birth: </label>
            <input required type="text" value={patientInput.dateOfBirth}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, dateOfBirth: event.target.value});
                   }}/>
            <Button variant="ok">Register</Button>
            <Button variant="normal">Cancel</Button>
        </PatientFormStyled>
    )
}