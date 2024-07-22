import {Patient} from "../models/Patient.ts";
import {CardActionContainer, CardActionLink, CardContainer} from "../styles/PatientCard.styled.ts";
import {useLocation} from "react-router-dom";
import Typography from "../styles/Typography.tsx";

type PatientCardProps = {
    patient: Patient;
}

export default function PatientCard(props: Readonly<PatientCardProps>) {
    const location = useLocation();

    return (
        <CardContainer>
            <div><Typography variant="h3">Name: </Typography>
                <Typography variant="base">{props.patient.lastname} {props.patient.firstname}</Typography></div>
            <div><Typography variant="h3">Date of birth: </Typography>
                <p>{props.patient.dateOfBirth}</p></div>
            <CardActionContainer>
                {location.pathname !== `/patients/${props.patient.id}` && (
                    <CardActionLink to={`/patients/${props.patient.id}`}>
                        <img alt="details-button" src={"/monitoring.png"} title="details"/>
                    </CardActionLink>
                )}
                {location.pathname !== `/patients/edit/${props.patient.id}` && (
                    <CardActionLink to={`/patients/edit/${props.patient.id}`}>
                        <img alt="edit-button" src={"/edit.png"} title="edit"/>
                    </CardActionLink>
                )}
                {location.pathname === `/patients/edit/${props.patient.id}` || location.pathname === `/patients/edit/${props.patient.id}` && (
                    <CardActionLink to={`/patients/edit/${props.patient.id}`}>
                        <img alt="delete-button" src={"/trash.png"} title="delete"/>
                    </CardActionLink>
                )}
            </CardActionContainer>
        </CardContainer>
    )
}