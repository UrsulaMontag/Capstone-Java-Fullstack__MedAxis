import { MedicalExamination } from "./HealthData";
import Typography from "../../styles/Typography";
import { formatDate } from "../../utils/formatDateView";

type ExaminationDetailsProps = {
    examination: MedicalExamination;
};

export default function ExaminationDetails(props: Readonly<ExaminationDetailsProps>) {
    return (
        <div>
            <Typography variant="h3">Examination Details</Typography>
            <Typography variant="h4">Date: {formatDate(props.examination.examinationDate)}</Typography>
            <Typography variant="h4">Diagnosis: {props.examination.diagnosis}</Typography>
        </div>
    );
}