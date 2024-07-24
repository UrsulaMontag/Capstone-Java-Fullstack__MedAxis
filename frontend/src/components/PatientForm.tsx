import usePatientStore from "../stores/usePatientStore.ts";
import {Patient, PatientToAdd} from "../models/Patient.ts";
import {ChangeEvent, FormEvent, useState} from "react";
import PatientFormStyled from "../styles/PatientForm.styled.tsx";
import Button from "../styles/Button.styled.tsx";
import {useLocation, useNavigate, useParams} from "react-router-dom";

export default function PatientForm() {
    const createPatient: (newPatient: PatientToAdd) => void = usePatientStore((state) => state.createPatient);
    const updatePatient: (id: string, newPatient: PatientToAdd) => void = usePatientStore(state => state.updatePatient);
    const patients: Patient[] = usePatientStore(state => state.patients);


    const urlParams = useParams();
    const id: string = urlParams.id || "";

    const currentPatient: Patient | undefined = patients.find(patient => patient.id === id);
    const initialInputState = {
        firstname: currentPatient ? currentPatient.firstname : "",
        lastname: currentPatient ? currentPatient.lastname : "",
        dateOfBirth: currentPatient ? currentPatient.dateOfBirth : ""
    };
    const [patientInput, setPatientInput] = useState<PatientToAdd>(initialInputState);

    const navigate = useNavigate();
    const location = useLocation();

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (location.pathname === "/patients/add") {
            createPatient(patientInput);
            alert(`New patient has been added successfully.`);
            navigate("/patients");
        }
        if (location.pathname === "/patients/edit/" + id) {
            updatePatient(id, patientInput);
            alert(`Patient has been updated successfully.`);
            navigate("/patients/" + id);
        }
        setPatientInput(initialInputState);
    }

    const handleCancel = (event: FormEvent<HTMLButtonElement>) => {
        event.preventDefault();
        location.pathname === "/patients/add" ?
            navigate("/patients")
            : navigate("/patients/" + id);
    }

    return (
        <PatientFormStyled onSubmit={(event: FormEvent<HTMLFormElement>) => handleSubmit(event)}>
            <label>Firstname:</label>
            <input required type="text" value={patientInput.firstname}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, firstname: event.target.value});
                   }}/>
            <label>Lastname:</label>
            <input required type="text" value={patientInput.lastname}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, lastname: event.target.value});
                   }}/>
            <label>Birthdate:</label>
            <input required type="date" value={patientInput.dateOfBirth}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, dateOfBirth: event.target.value});
                   }}/>
            <div>
                <Button variant="ok"><img alt="ok-button save"
                                          src={"/ok.png"}
                                          title="Enter edit"/></Button>
                <Button variant="normal" onClick={handleCancel}><img alt="cancel-button"
                                                                     src={"/cancel.png"}
                                                                     title="Cancel"/></Button>
            </div>
        </PatientFormStyled>
    )
}