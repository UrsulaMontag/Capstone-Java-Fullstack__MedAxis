import {Patient} from "../../models/patient/Patient.ts";
import {CardActionContainer, CardContainer, NumberEntry} from "../../styles/PatientCard.styled.ts";
import {useLocation, useNavigate} from "react-router-dom";
import Typography from "../../styles/Typography.tsx";
import usePatientStore from "../../stores/usePatientStore.ts";
import Button from "../../styles/Button.styled.tsx";
import PatientCardDetails from "./PatientCardDetails.tsx";
import PatientHealthCard from "../health-data/PatientHealthCard.tsx";
import useGlobalStore from "../../stores/useGloblaStore.ts";

type PatientCardProps = {
    patient: Patient;
    detailed: boolean,
    listNr?: number
}

export default function PatientCard(props: Readonly<PatientCardProps>) {
    const {patient, detailed, listNr} = props;
    const location = useLocation();
    const navigate = useNavigate();
    const userRole: ("nurse" | "doctor" | null) = useGlobalStore(state => state.userRole)
    const deletePatient: (id: string) => void = usePatientStore(state => state.deletePatient);
    const isViewingPatientDetails = location.pathname === `/patients/${patient.id}`;
    const isEditingPatient = location.pathname === `/patients/edit/${patient.id}`;
    const isPatientList = location.pathname === `/patients`;
    const isViewingPatientHealthDetails = location.pathname === `/health_data/${patient.healthDataId}`;


    const handleDelete = () => {
        const userConfirmed = window.confirm("Are you sure you want to delete this Patient?");
        if (userConfirmed) {
            deletePatient(patient.id);
            navigate("/patients");
        }
    }

    const handleDetailNav = () => {
        userRole === "nurse"
            ? navigate(`/patients/${patient.id}`)
            : userRole === "doctor" ?
                navigate(`/health_data/${patient.healthDataId}`)
                : navigate("/");
    }
    const handleEditNav = () => {
        userRole === "nurse"
            ? navigate(`/patients/edit/${patient.id}`)
            : userRole === "doctor" ?
                navigate(`/health_data/${patient.healthDataId}/add-icd-details`)
                : navigate("/");
    }

    return (
        <CardContainer details={detailed}>
            {detailed && userRole === "nurse" ? (
                <PatientCardDetails patient={patient}/>
            ) : detailed && userRole === "doctor" ? (
                <PatientHealthCard patient={patient}/>
            ) : (
                <>
                    <NumberEntry>{listNr}</NumberEntry>
                    <Typography variant="base">{patient.lastname} {patient.firstname}</Typography>
                    <Typography variant="base">{new Date(patient.dateOfBirth).toLocaleDateString()}</Typography>
                </>
            )}
            <CardActionContainer details={detailed}>
                {!isViewingPatientDetails && !isViewingPatientHealthDetails && (
                    <Button variant="normal" onClick={handleDetailNav}>
                        <img alt="details-button" src={"/monitoring.png"} title="details"/>
                    </Button>
                )}
                {!isEditingPatient && (
                    <Button variant="normal" onClick={handleEditNav}>
                        <img alt="edit-button" src={"/edit.png"} title="edit"/>
                    </Button>
                )}
                {!isPatientList && !isViewingPatientHealthDetails && (
                    <Button variant="delete" onClick={handleDelete}>
                        <img alt="delete-button" src={"/trash.png"} title="delete"/>
                    </Button>
                )}
                {isViewingPatientDetails || isViewingPatientHealthDetails && (
                    <Button variant="normal" onClick={() => navigate(`/patients`)}>
                        <img alt="cancel-button" src={"/cancel.png"} title="cancel"/>
                    </Button>
                )}
            </CardActionContainer>
        </CardContainer>

    )
}