import {Patient} from "../../models/patient/Patient.ts";
import Typography from "../../styles/Typography.tsx";

type PatientCardDetailsProps = {
    patient: Patient;
}

export default function PatientCardDetails(props: Readonly<PatientCardDetailsProps>) {
    const patient = props.patient;
    const formatDate = (dateString: string) => {
        const options: Intl.DateTimeFormatOptions = {year: 'numeric', month: '2-digit', day: '2-digit'};
        return new Date(dateString).toLocaleDateString('en-US', options);
    };

    return (
        <>
            <Typography variant="h3">Name: </Typography>
            <Typography variant="base">{patient.lastname} {patient.firstname}</Typography>
            <Typography variant="h3">Birthdate: </Typography>
            <Typography variant="base">{formatDate(patient.dateOfBirth)}</Typography>
            <Typography variant="h3">Insurance Nr: </Typography>
            <Typography variant="base">{patient.insuranceNr}</Typography>
            <Typography variant="h3">Contact Information: </Typography>
            <br/>
            {patient.contactInformation.phoneNr && (
                <>
                    <Typography variant="h4">Phone Nr: </Typography>
                    <Typography variant="base">{patient.contactInformation.phoneNr}</Typography>
                </>
            )}
            {patient.contactInformation.email && (
                <>
                    <Typography variant="h4">Email: </Typography>
                    <Typography variant="base">{patient.contactInformation.email}</Typography>
                </>
            )}
            <Typography variant="h4">Address: </Typography>
            <Typography variant="base">{patient.contactInformation.address}</Typography>
            <br/>
            <Typography variant="base">{patient.contactInformation.town}</Typography>
        </>
    );
}