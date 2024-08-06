import usePatientStore from "../../stores/usePatientStore.ts";
import {Patient, PatientToAdd} from "../../models/patient/Patient.ts";
import {ChangeEvent, FormEvent, useState} from "react";
import PatientFormStyled from "../../styles/PatientForm.styled.tsx";
import Button from "../../styles/Button.styled.tsx";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import validation from "../../utils/dataValidation.ts";
import Typography from "../../styles/Typography.tsx";

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
        alert("Your input contains invalid formats. Please fill in the right data format.")
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
            <br/>
            {errors.firstname && <Typography variant="error-info">{errors.firstname}</Typography>}

            <label>Lastname:</label>
            <input required type="text" value={patientInput.lastname}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, lastname: event.target.value});
                   }}/>
            <br/>
            {errors.lastname && <Typography variant="error-info">{errors.lastname}</Typography>}

            <label>Birthdate:</label>
            <input required type="date" value={patientInput.dateOfBirth} min="1900-01-01"
                   max={Date.now()}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, dateOfBirth: event.target.value});
                   }}/>
            <br/>
            {errors.dateOfBirth && <Typography variant="error-info">{errors.dateOfBirth}</Typography>}

            <label>Insurance Nr:</label>
            <input required type="text" value={patientInput.insuranceNr}
                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                       setPatientInput({...patientInput, insuranceNr: event.target.value});
                   }}/>
            <br/>
            {errors.insuranceNr && <Typography variant="error-info">{errors.insuranceNr}</Typography>}

            <label>Phone:</label>
            <input
                type="text"
                name="phoneNr"
                value={patientInput.contactInformation.phoneNr}
                onChange={handleInputChange}
            />
            <br/>
            {errors.phone && <Typography variant="error-info">{errors.phone}</Typography>}

            <label>Email:</label>
            <input
                type="email"
                name="email"
                value={patientInput.contactInformation.email}
                onChange={handleInputChange}
            />
            <br/>
            {errors.email && <Typography variant="error-info">{errors.email}</Typography>}

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