import {Patient} from "../models/Patient.ts";
import {CardActionContainer, CardContainer, NumberEntry} from "../styles/PatientCard.styled.ts";
import {useLocation, useNavigate} from "react-router-dom";
import Typography from "../styles/Typography.tsx";
import usePatientStore from "../stores/usePatientStore.ts";
import Button from "../styles/Button.styled.tsx";

type PatientCardProps = {
    patient: Patient;
    detailed: boolean,
    listNr?: number
}

export default function PatientCard(props: Readonly<PatientCardProps>) {
    const location = useLocation();
    const navigate = useNavigate();
    const deletePatient: (id: string) => void = usePatientStore(state => state.deletePatient);
    const formatDate = (dateString: string) => {
        const options: Intl.DateTimeFormatOptions = {year: 'numeric', month: '2-digit', day: '2-digit'};
        return new Date(dateString).toLocaleDateString('en-US', options);
    };

    const handleDelete = () => {
        deletePatient(props.patient.id);
        navigate("/patients");
    }
    const handleNavigation = (path: string) => {
        navigate(path);
    }
    return (
        <CardContainer details={props.detailed}>
            {props.detailed
                ? (<>
                    <Typography variant="h3">Name: </Typography>
                    <Typography variant="base">{props.patient.lastname} {props.patient.firstname}</Typography>
                    <Typography variant="h3">Birthdate: </Typography>
                    <Typography variant="base">{formatDate(props.patient.dateOfBirth)}</Typography>
                </>)
                : (<>
                    <NumberEntry>{props.listNr}</NumberEntry>
                    <Typography variant="base">{props.patient.lastname} {props.patient.firstname}</Typography>
                    <Typography variant="base">{formatDate(props.patient.dateOfBirth)}</Typography>
                </>)
            }

            <CardActionContainer details={props.detailed}>
                {location.pathname !== `/patients/${props.patient.id}` && (
                    <Button variant="normal" onClick={() => handleNavigation(`/patients/${props.patient.id}`)}>
                        <img alt="details-button" src={"/monitoring.png"} title="details"/>
                    </Button>
                )}
                {location.pathname !== `/patients/edit/${props.patient.id}` && (
                    <Button variant="normal" onClick={() => handleNavigation(`/patients/edit/${props.patient.id}`)}>
                        <img alt="edit-button" src={"/edit.png"} title="edit"/>
                    </Button>
                )}
                {location.pathname !== `/patients` && (
                    <Button variant="delete" onClick={handleDelete}>
                        <img alt="delete-button" src={"/trash.png"} title="delete"/>
                    </Button>
                )}
            </CardActionContainer>
        </CardContainer>

    )
}