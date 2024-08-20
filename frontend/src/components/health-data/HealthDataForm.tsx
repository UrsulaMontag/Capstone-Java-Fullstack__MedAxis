import {Patient} from "../../models/patient/Patient.ts";
import usePatientStore from "../../stores/usePatientStore.ts";
import {useNavigate, useParams} from "react-router-dom";
import {FormEvent, useState} from "react";
import FormStyled from "../../styles/Form.styled.tsx";
import Typography from "../../styles/Typography.tsx";
import {formatDate} from "../../utils/formatDateView.ts";
import {IcdECTWrapper} from "../icd_api/IcdECT.tsx";
import {CardContainer} from "../../styles/PatientCard.styled.ts";

export default function HealthDataForm() {
    const urlParams = useParams();
    const navigate = useNavigate();

    const patients: Patient[] = usePatientStore(state => state.patients);

    const currentPatient: Patient | undefined = patients.find(patient => patient.healthDataId === urlParams.healthDataId);

    const [isIcdInputClicked, setIsIcdInputClicked] = useState(false);

    const handleIcdInputClick = () => {
        setIsIcdInputClicked(true);
    };

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        navigate("/health_data/" + currentPatient?.healthDataId);
    }
    const handleCancel = (event: FormEvent<HTMLButtonElement>) => {
        event.preventDefault();
        navigate("/health_data/" + currentPatient?.healthDataId);
    }

    return (
        <>
            {currentPatient && <>
                <CardContainer details={true}>
                    <Typography variant="h3">Name: </Typography>
                    <Typography variant="base">{currentPatient.lastname} {currentPatient.firstname}</Typography>
                    <Typography variant="h3">Birthdate: </Typography>
                    <Typography variant="base">{formatDate(currentPatient.dateOfBirth)}</Typography>
                    <Typography variant="h3">Add ICD codes via WHO-API:</Typography>
                    <FormStyled onSubmit={(event: FormEvent<HTMLFormElement>) => handleSubmit(event)}>
                        {!isIcdInputClicked &&
                            <input type="text" onClick={handleIcdInputClick} readOnly
                                   placeholder="Click to add ICD code"/>}
                        {isIcdInputClicked && (
                            <IcdECTWrapper/>
                        )}

                        <button type="submit">Save</button>
                        <button type="button" onClick={handleCancel}>Cancel</button>
                    </FormStyled></CardContainer></>
            }

        </>

    )
}