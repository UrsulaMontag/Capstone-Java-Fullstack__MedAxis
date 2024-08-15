import {Patient} from "../../models/patient/Patient.ts";
import {CardActionContainer, CardContainer, NumberEntry} from "../../styles/PatientCard.styled.ts";
import {useLocation, useNavigate} from "react-router-dom";
import Typography from "../../styles/Typography.tsx";
import usePatientStore from "../../stores/usePatientStore.ts";
import Button from "../../styles/Button.styled.tsx";
import PatientCardDetails from "./PatientCardDetails.tsx";

type PatientCardProps = {
    patient: Patient;
    detailed: boolean,
    listNr?: number
}

export default function PatientCard(props: Readonly<PatientCardProps>) {
    const {patient, detailed, listNr} = props;
    const location = useLocation();
    const navigate = useNavigate();
    const deletePatient: (id: string) => void = usePatientStore(state => state.deletePatient);
    const isViewingPatientDetails = location.pathname === `/patients/${patient.id}`;
    const isEditingPatient = location.pathname === `/patients/edit/${patient.id}`;
    const isPatientList = location.pathname === `/patients`;


    const handleDelete = () => {
        const userConfirmed = window.confirm("Are you sure you want to delete this Patient?");
        if (userConfirmed) {
            deletePatient(patient.id);
            navigate("/patients");
        }
    }

    return (
        <CardContainer details={detailed}>
            {detailed ? (
                <PatientCardDetails patient={patient}/>
            ) : (
                <>
                    <NumberEntry>{listNr}</NumberEntry>
                    <Typography variant="base">{patient.lastname} {patient.firstname}</Typography>
                    <Typography variant="base">{new Date(patient.dateOfBirth).toLocaleDateString()}</Typography>
                </>
            )}
            <CardActionContainer details={detailed}>
                {!isViewingPatientDetails && (
                    <Button variant="normal" onClick={() => navigate(`/patients/${patient.id}`)}>
                        <img alt="details-button" src={"/monitoring.png"} title="details"/>
                    </Button>
                )}
                {!isEditingPatient && (
                    <Button variant="normal" onClick={() => navigate(`/patients/edit/${patient.id}`)}>
                        <img alt="edit-button" src={"/edit.png"} title="edit"/>
                    </Button>
                )}
                {!isPatientList && (
                    <Button variant="delete" onClick={handleDelete}>
                        <img alt="delete-button" src={"/trash.png"} title="delete"/>
                    </Button>
                )}
                {isViewingPatientDetails && (
                    <Button variant="normal" onClick={() => navigate(`/patients`)}>
                        <img alt="cancel-button" src={"/cancel.png"} title="cancel"/>
                    </Button>
                )}
            </CardActionContainer>
        </CardContainer>

    )
}