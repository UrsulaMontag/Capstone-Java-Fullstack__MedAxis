import {Patient} from "../models/Patient.ts";
import {CardActionContainer, CardActionLink, CardContainer, NumberEntry} from "../styles/PatientCard.styled.ts";
import {useLocation} from "react-router-dom";
import Typography from "../styles/Typography.tsx";
import {MainContent} from "../styles/MainContent.ts";

type PatientCardProps = {
    patient: Patient;
    detailed: boolean,
    listNr?: number
}

export default function PatientCard(props: Readonly<PatientCardProps>) {
    const location = useLocation();

    const formatDate = (dateString: string) => {
        const options: Intl.DateTimeFormatOptions = {year: 'numeric', month: 'numeric', day: 'numeric'};
        return new Date(dateString).toLocaleDateString(undefined, options);
    };
    return (
        <MainContent>
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
                        <CardActionLink to={`/patients/${props.patient.id}`}>
                            <img alt="details-button" src={"/monitoring.png"} title="details"/>
                        </CardActionLink>
                    )}
                    {location.pathname !== `/patients/edit/${props.patient.id}` && (
                        <CardActionLink to={`/patients/edit/${props.patient.id}`}>
                            <img alt="edit-button" src={"/edit.png"} title="edit"/>
                        </CardActionLink>
                    )}
                    {location.pathname !== `/patients` && (
                        <CardActionLink to={`/patients/edit/${props.patient.id}`}>
                            <img alt="delete-button" src={"/trash.png"} title="delete"/>
                        </CardActionLink>
                    )}
                </CardActionContainer>
            </CardContainer></MainContent>

    )
}