import usePatientStore from "../stores/usePatientStore.ts";
import {Patient, PatientToAdd} from "../models/Patient.ts";
import {ChangeEvent, FormEvent, useState} from "react";
import PatientFormStyled from "../styles/PatientForm.styled.tsx";
import Button from "../styles/Button.styled.tsx";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import validation from "../utils/dataValidation.ts";

export default function PatientForm() {
    const {createPatient, updatePatient} = usePatientStore((state) => ({
        createPatient: state.createPatient,
        updatePatient: state.updatePatient,
        fetchPatients: state.fetchPatients
    }));
    const patients: Patient[] = usePatientStore(state => state.patients);

    const urlParams = useParams();
    const id: string = urlParams.id || "";

    const currentPatient: Patient | undefined = patients.find(patient => patient.id === id);
    const initialInputState = {
        firstname: currentPatient ? currentPatient.firstname : "",
        lastname: currentPatient ? currentPatient.lastname : "",
        dateOfBirth: currentPatient ? currentPatient.dateOfBirth : "",
        insuranceNr: currentPatient ? currentPatient.insuranceNr : "",
        contactInformation: currentPatient ? currentPatient.contactInformation : {
            phoneNr: "",
            email: "",
            address: "",
            town: ""
        }
    };
    const [patientInput, setPatientInput] = useState<PatientToAdd>(initialInputState);
    const [errors, setErrors] = useState<{ [key: string]: string }>({});

    const navigate = useNavigate();
    const location = useLocation();

    const validateForm = (): boolean => {
        const newErrors: { [key: string]: string } = {};
        let isValid = true;

        if (!validation.isValidName(patientInput.firstname)) {
            newErrors.firstname = "Invalid first name format.";
            isValid = false;
        }
        if (!validation.isValidName(patientInput.lastname)) {
            newErrors.lastname = "Invalid last name format.";
            isValid = false;
        }
        if (!validation.isValidDateOfBirth(patientInput.dateOfBirth)) {
            newErrors.dateOfBirth = "Invalid date of birth.";
            isValid = false;
        }
        if (!validation.isValidInsuranceNumber(patientInput.insuranceNr)) {
            newErrors.insuranceNr = "Invalid insurance number format.";
            isValid = false;
        }
        if (patientInput.contactInformation.phoneNr && !validation.isValidPhoneNumber(patientInput.contactInformation.phoneNr)) {
            newErrors.phone = "Invalid phoneNr number format.";
            isValid = false;
        }
        if (patientInput.contactInformation.email && !validation.isValidEmail(patientInput.contactInformation.email)) {
            newErrors.email = "Invalid email address format.";
            isValid = false;
        }
        setErrors(newErrors);
        return isValid;
    };


    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (validateForm()) {
            if (location.pathname === "/patients/add") {
                createPatient(patientInput);
            } else if (location.pathname === "/patients/edit/" + id) {
                updatePatient(id, patientInput);
            }
            navigate("/patients");
            setPatientInput(initialInputState);
        }
    }
    const handleCancel = (event: FormEvent<HTMLButtonElement>) => {
        event.preventDefault();
        location.pathname === "/patients/add" ?
            navigate("/patients")
            : navigate("/patients/" + id);
    }

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setPatientInput((prevState) => ({
            ...prevState,
            contactInformation: {
                ...prevState.contactInformation,
                [name]: value
            }
        }));
    };

    return (
        <PatientFormStyled onSubmit={(event: FormEvent<HTMLFormElement>) => handleSubmit(event)}>
            <label>Firstname:</label>
            <input required type="text" value={patientInput.firstname}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, firstname: event.target.value});
                   }}/>
            {errors.firstname && <p>{errors.firstname}</p>}

            <label>Lastname:</label>
            <input required type="text" value={patientInput.lastname}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, lastname: event.target.value});
                   }}/>
            {errors.lastname && <p>{errors.lastname}</p>}

            <label>Birthdate:</label>
            <input required type="date" value={patientInput.dateOfBirth} min="1900-01-01"
                   max={Date.now()}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, dateOfBirth: event.target.value});
                   }}/>
            {errors.dateOfBirth && <p>{errors.dateOfBirth}</p>}

            <label>Insurance Nr:</label>
            <input required type="text" value={patientInput.insuranceNr}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, insuranceNr: event.target.value});
                   }}/>
            {errors.insuranceNr && <p>{errors.insuranceNr}</p>}

            <label>Phone:</label>
            <input
                type="text"
                name="phoneNr"
                value={patientInput.contactInformation.phoneNr}
                onChange={handleInputChange}
            />
            {errors.phone && <p>{errors.phone}</p>}

            <label>Email:</label>
            <input
                type="email"
                name="email"
                value={patientInput.contactInformation.email}
                onChange={handleInputChange}
            />
            {errors.email && <p>{errors.email}</p>}

            <label>Address:</label>
            <input
                type="text"
                name="address"
                value={patientInput.contactInformation.address}
                onChange={handleInputChange}
            />

            <label>Town:</label>
            <input
                type="text"
                name="town"
                value={patientInput.contactInformation.town}
                onChange={handleInputChange}
            />


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