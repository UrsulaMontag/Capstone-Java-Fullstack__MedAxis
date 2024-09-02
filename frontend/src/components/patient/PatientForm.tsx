import usePatientStore from "../../stores/usePatientStore.ts";
import {Patient, PatientToAdd} from "../../models/patient/Patient.ts";
import {ChangeEvent, FormEvent, useState} from "react";
import FormStyled from "../../styles/Form.styled.tsx";
import Button from "../../styles/Button.styled.tsx";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import validation from "../../utils/dataValidation.ts";
import Typography from "../../styles/Typography.tsx";

export default function PatientForm() {
    const {createPatient, updatePatient} = usePatientStore((state) => ({
        createPatient: state.createPatient,
        updatePatient: state.updatePatient,
    }));
    const patients: Patient[] = usePatientStore(state => state.patients);

    const urlParams = useParams();
    const id: string = urlParams.id || "";

    const currentPatient: Patient | undefined = patients.find(patient => patient.id === id);
    const initialInputState = {
        firstname: currentPatient ? currentPatient.firstname : "",
        lastname: currentPatient ? currentPatient.lastname : "",
        dateOfBirth: currentPatient ? currentPatient.dateOfBirth : "",
        gender: currentPatient ? currentPatient.gender : "",
        nationality: currentPatient ? currentPatient.nationality : "",
        maritalStatus: currentPatient ? currentPatient.maritalStatus : "",
        primaryLanguage: currentPatient ? currentPatient.primaryLanguage : "",
        occupation: currentPatient ? currentPatient.occupation : "",
        emergencyContact: currentPatient ? currentPatient.emergencyContact : {
            name: "",
            relationship: "",
            phoneNumber: ""
        },
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

        const validations = [
            {
                field: 'firstname',
                method: validation.isValidName,
                validWhen: true,
                message: 'Invalid first name format.',
                value: patientInput.firstname
            },
            {
                field: 'lastname',
                method: validation.isValidName,
                validWhen: true,
                message: 'Invalid last name format.',
                value: patientInput.lastname
            },
            {
                field: 'dateOfBirth',
                method: validation.isValidDateOfBirth,
                validWhen: true,
                message: 'Invalid date of birth.',
                value: patientInput.dateOfBirth
            },
            {
                field: 'gender',
                method: validation.isValidGender,
                validWhen: true,
                message: 'Invalid gender format.',
                value: patientInput.gender
            },
            {
                field: 'nationality',
                method: validation.isValidNationality,
                validWhen: true,
                message: 'Invalid nationality format.',
                value: patientInput.nationality
            },
            {
                field: 'maritalStatus',
                method: validation.isValidMaritalStatus,
                validWhen: true,
                message: 'Invalid marital format.',
                value: patientInput.maritalStatus
            },
            {
                field: 'primaryLanguage',
                method: validation.isValidPrimaryLanguage,
                validWhen: true,
                message: 'Invalid language format.',
                value: patientInput.primaryLanguage
            },
            {
                field: 'occupation',
                method: validation.isValidOccupation,
                validWhen: true,
                message: 'Invalid occupation format.',
                value: patientInput.occupation
            },
            {
                field: 'insuranceNr',
                method: validation.isValidInsuranceNumber,
                validWhen: true,
                message: 'Invalid insurance number format.',
                value: patientInput.insuranceNr
            },
            {
                field: 'phoneNr',
                method: validation.isValidPhoneNumber,
                validWhen: patientInput.contactInformation.phoneNr !== undefined,
                message: 'Invalid phone number format.',
                value: patientInput.contactInformation.phoneNr
            },
            {
                field: 'email',
                method: validation.isValidEmail,
                validWhen: patientInput.contactInformation.email !== undefined,
                message: 'Invalid email address format.',
                value: patientInput.contactInformation.email
            }
        ];

        validations.forEach(validation => {
            if (validation.value !== undefined && validation.method(validation.value) !== validation.validWhen) {
                newErrors[validation.field] = validation.message;
                isValid = false;
            }
        });

        if (!validation.isValidEmergencyContact(patientInput.emergencyContact)) {
            newErrors.emergencyContact = 'Invalid emergency contact format.';
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
            [name]: value
        }));
    };
    const handleContactInformationChange = (event: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setPatientInput((prevState) => ({
            ...prevState,
            contactInformation: {
                ...prevState.contactInformation,
                [name]: value,
            },
        }));
    };

    const handleEmergencyContactChange = (event: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setPatientInput((prevState) => ({
            ...prevState,
            emergencyContact: {
                ...prevState.emergencyContact,
                [name]: value,
            },
        }));
    };
    const handleSelectChange = (event: ChangeEvent<HTMLSelectElement>) => {
        const {name, value} = event.target;
        setPatientInput((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    return (
        <FormStyled onSubmit={(event: FormEvent<HTMLFormElement>) => handleSubmit(event)}>
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

            <label>Gender:</label>
            <select name="gender" id="gender-select" value={patientInput.gender} onChange={handleSelectChange}>
                <option value="">--Please choose gender--</option>
                <option value="female">Female</option>
                <option value="male">Male</option>
                <option value="other">Other</option>
            </select>
            <br/>
            {errors.gender && <Typography variant="error-info">{errors.gender}</Typography>}

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
                onChange={handleContactInformationChange}
            />
            <br/>
            {errors.phone && <Typography variant="error-info">{errors.phone}</Typography>}

            <label>Email:</label>
            <input
                type="email"
                name="email"
                value={patientInput.contactInformation.email}
                onChange={handleContactInformationChange}
            />
            <br/>
            {errors.email && <Typography variant="error-info">{errors.email}</Typography>}

            <label>Address:</label>
            <input
                type="text"
                name="address"
                value={patientInput.contactInformation.address}
                onChange={handleContactInformationChange}
            />

            <label>Town:</label>
            <input
                type="text"
                name="town"
                value={patientInput.contactInformation.town}
                onChange={handleContactInformationChange}
            />

            <label>Emergency Contact:</label>
            <br/>
            <label>Name:</label>
            <input
                type="text"
                name="name"
                value={patientInput.emergencyContact.name}
                onChange={handleEmergencyContactChange}
            />

            <label>Relationship:</label>
            <input
                type="text"
                name="relationship"
                value={patientInput.emergencyContact.relationship}
                onChange={handleEmergencyContactChange}
            />
            <label>Phone:</label>
            <input
                type="text"
                name="phoneNumber"
                value={patientInput.emergencyContact.phoneNumber}
                onChange={handleEmergencyContactChange}
            />
            <br/>
            {errors.emergencyContact && <Typography variant="error-info">{errors.emergencyContact}</Typography>}

            <label>Nationality:</label>
            <input
                type="text"
                name="nationality"
                value={patientInput.nationality}
                onChange={handleInputChange}
            />
            <br/>
            {errors.nationality && <Typography variant="error-info">{errors.nationality}</Typography>}

            <label>Marital Status:</label>
            <select name="maritalStatus" id="marital-status-select" value={patientInput.maritalStatus}
                    onChange={handleSelectChange}>
                <option value="">--Please choose marital status--</option>
                <option value="single">Single</option>
                <option value="married">Married</option>
                <option value="divorced">Divorced</option>
                <option value="widowed">Widowed</option>
            </select>
            <br/>
            {errors.maritalStatus && <Typography variant="error-info">{errors.maritalStatus}</Typography>}

            <label>Primary Language:</label>
            <input
                type="text"
                name="primaryLanguage"
                value={patientInput.primaryLanguage}
                onChange={handleInputChange}
            />
            <br/>
            {errors.primaryLanguage && <Typography variant="error-info">{errors.primaryLanguage}</Typography>}

            <label>Occupation:</label>
            <input
                type="text"
                name="occupation"
                value={patientInput.occupation}
                onChange={handleInputChange}
            />
            <br/>
            {errors.primaryLanguage && <Typography variant="error-info">{errors.primaryLanguage}</Typography>}

            <div>
                <Button variant="ok"><img alt="ok-button save"
                                          src={"/ok.png"}
                                          title="Enter edit"/></Button>
                <Button variant="normal" onClick={handleCancel}><img alt="cancel-button"
                                                                     src={"/cancel.png"}
                                                                     title="Cancel"/></Button>
            </div>
        </FormStyled>
    )
}