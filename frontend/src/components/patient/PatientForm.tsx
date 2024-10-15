import usePatientStore from "../../stores/usePatientStore.ts";
import { Patient, PatientToAdd } from "../../models/patient/Patient.ts";
import { ChangeEvent, FormEvent, useState } from "react";
import { StyledForm, FormGroup, FormLabel, FormInput, FormHeading, ErrorInfo } from "../../styles/Form.styled.tsx";
import Button from "../../styles/Button.styled.tsx";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import validation from "../../utils/dataValidation.ts";


export default function PatientForm() {
    const { createPatient, updatePatient } = usePatientStore((state) => ({
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
        const { name, value } = event.target;
        setPatientInput((prevState) => ({
            ...prevState,
            [name]: value
        }));
    };
    const handleContactInformationChange = (event: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setPatientInput((prevState) => ({
            ...prevState,
            contactInformation: {
                ...prevState.contactInformation,
                [name]: value,
            },
        }));
    };

    const handleEmergencyContactChange = (event: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setPatientInput((prevState) => ({
            ...prevState,
            emergencyContact: {
                ...prevState.emergencyContact,
                [name]: value,
            },
        }));
    };
    const handleSelectChange = (event: ChangeEvent<HTMLSelectElement>) => {
        const { name, value } = event.target;
        setPatientInput((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    return (
        <StyledForm onSubmit={(event: FormEvent<HTMLFormElement>) => handleSubmit(event)}>
            <FormGroup>
                <FormLabel>Firstname:</FormLabel>
                <FormInput
                    required
                    type="text"
                    value={patientInput.firstname}
                    onChange={(event: ChangeEvent<HTMLInputElement>) => {
                        setPatientInput({ ...patientInput, firstname: event.target.value });
                    }}
                />
            </FormGroup>
            {errors.firstname && <ErrorInfo>{errors.firstname}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Lastname:</FormLabel>
                <FormInput
                    required
                    type="text"
                    value={patientInput.lastname}
                    onChange={(event: ChangeEvent<HTMLInputElement>) => {
                        setPatientInput({ ...patientInput, lastname: event.target.value });
                    }}
                />
            </FormGroup>
            {errors.lastname && <ErrorInfo>{errors.lastname}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Birthdate:</FormLabel>
                <FormInput
                    required
                    type="date"
                    value={patientInput.dateOfBirth}
                    min="1900-01-01"
                    max={Date.now()}
                    onChange={(event: ChangeEvent<HTMLInputElement>) => {
                        setPatientInput({ ...patientInput, dateOfBirth: event.target.value });
                    }}
                />
            </FormGroup>
            {errors.dateOfBirth && <ErrorInfo>{errors.dateOfBirth}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Gender:</FormLabel>
                <FormInput
                    as="select"
                    name="gender"
                    id="gender-select"
                    value={patientInput.gender}
                    onChange={handleSelectChange}
                >
                    <option value="">--Please choose gender--</option>
                    <option value="female">Female</option>
                    <option value="male">Male</option>
                    <option value="other">Other</option>
                </FormInput>
            </FormGroup>
            {errors.gender && <ErrorInfo>{errors.gender}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Insurance Nr:</FormLabel>
                <FormInput
                    required
                    type="text"
                    value={patientInput.insuranceNr}
                    onChange={(event: ChangeEvent<HTMLInputElement>) => {
                        setPatientInput({ ...patientInput, insuranceNr: event.target.value });
                    }}
                />
            </FormGroup>
            {errors.insuranceNr && <ErrorInfo>{errors.insuranceNr}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Phone:</FormLabel>
                <FormInput
                    type="text"
                    name="phoneNr"
                    value={patientInput.contactInformation.phoneNr}
                    onChange={handleContactInformationChange}
                />
            </FormGroup>
            {errors.phone && <ErrorInfo>{errors.phone}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Email:</FormLabel>
                <FormInput
                    type="email"
                    name="email"
                    value={patientInput.contactInformation.email}
                    onChange={handleContactInformationChange}
                />
            </FormGroup>
            {errors.email && <ErrorInfo>{errors.email}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Address:</FormLabel>
                <FormInput
                    type="text"
                    name="address"
                    value={patientInput.contactInformation.address}
                    onChange={handleContactInformationChange}
                />
            </FormGroup>

            <FormGroup>
                <FormLabel>Town:</FormLabel>
                <FormInput
                    type="text"
                    name="town"
                    value={patientInput.contactInformation.town}
                    onChange={handleContactInformationChange}
                />
            </FormGroup>

            <FormHeading>Emergency Contact:</FormHeading>
            <FormGroup>
                <FormLabel>Name:</FormLabel>
                <FormInput
                    type="text"
                    name="name"
                    value={patientInput.emergencyContact.name}
                    onChange={handleEmergencyContactChange}
                />
            </FormGroup>

            <FormGroup>
                <FormLabel>Relationship:</FormLabel>
                <FormInput
                    type="text"
                    name="relationship"
                    value={patientInput.emergencyContact.relationship}
                    onChange={handleEmergencyContactChange}
                />
            </FormGroup>

            <FormGroup>
                <FormLabel>Phone:</FormLabel>
                <FormInput
                    type="text"
                    name="phoneNumber"
                    value={patientInput.emergencyContact.phoneNumber}
                    onChange={handleEmergencyContactChange}
                />
            </FormGroup>
            {errors.emergencyContact && <ErrorInfo>{errors.emergencyContact}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Nationality:</FormLabel>
                <FormInput
                    type="text"
                    name="nationality"
                    value={patientInput.nationality}
                    onChange={handleInputChange}
                />
            </FormGroup>
            {errors.nationality && <ErrorInfo>{errors.nationality}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Marital Status:</FormLabel>
                <FormInput
                    as="select"
                    name="maritalStatus"
                    id="marital-status-select"
                    value={patientInput.maritalStatus}
                    onChange={handleSelectChange}
                >
                    <option value="">--Please choose marital status--</option>
                    <option value="single">Single</option>
                    <option value="married">Married</option>
                    <option value="divorced">Divorced</option>
                    <option value="widowed">Widowed</option>
                </FormInput>
            </FormGroup>
            {errors.maritalStatus && <ErrorInfo>{errors.maritalStatus}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Primary Language:</FormLabel>
                <FormInput
                    type="text"
                    name="primaryLanguage"
                    value={patientInput.primaryLanguage}
                    onChange={handleInputChange}
                />
            </FormGroup>
            {errors.primaryLanguage && <ErrorInfo>{errors.primaryLanguage}</ErrorInfo>}

            <FormGroup>
                <FormLabel>Occupation:</FormLabel>
                <FormInput
                    type="text"
                    name="occupation"
                    value={patientInput.occupation}
                    onChange={handleInputChange}
                />
            </FormGroup>
            {errors.occupation && <ErrorInfo>{errors.occupation}</ErrorInfo>}

            <div>
                <Button variant="ok"><img alt="ok-button save" src={"/ok.png"} title="Enter edit" /></Button>
                <Button variant="normal" onClick={handleCancel}><img alt="cancel-button" src={"/cancel.png"} title="Cancel" /></Button>
            </div>
        </StyledForm>
    )
}