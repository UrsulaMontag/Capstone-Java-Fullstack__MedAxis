import {useNavigate} from "react-router-dom";
import Typography from "../styles/Typography.tsx";
import {Patient} from "../models/patient/Patient.ts";
import usePatientStore from "../stores/usePatientStore.ts";
import {ButtonContainer, ButtonSectionContainer, StatisticsContainer} from "../styles/Dashboard.styled.ts";

export default function Dashboard() {
    const patients: Patient[] = usePatientStore(state => state.patients);
    const navigate = useNavigate();
    const handleNavigate = (path: string) => {
        navigate(path);
    }
    return (
        <section>
            <ButtonSectionContainer>
                <ButtonContainer tabIndex={0} onClick={() => handleNavigate("/patients")}>
                    <Typography variant="button-field-header">Patients</Typography>
                    <img src="/patients.png" alt="persons image"/>
                    <div>
                        <Typography variant="button-field-info">Total: {patients.length}</Typography>
                        <Typography variant="button-field-info">New: 6</Typography></div>
                </ButtonContainer>
                <ButtonContainer tabIndex={0} onClick={() => handleNavigate("/icd")}>
                    <Typography variant="button-field-header">ICD-API-Data</Typography>
                    <img src="/health.png" alt="bio image"/>
                    <div>
                        <Typography variant="button-field-info">WHO ICD-API v 2.4</Typography>
                        <Typography variant="button-field-info">ECT v 1.7.1</Typography></div>
                </ButtonContainer>
                <ButtonContainer tabIndex={0} onClick={() => handleNavigate("/")}>
                    <Typography variant="button-field-header">Staff Internal</Typography>
                    <img src="/docs.png" alt="staff image"/>
                    <div>
                        <Typography variant="button-field-info">Doctors: 10</Typography>
                        <Typography variant="button-field-info">Available: 6</Typography>
                    </div>
                    <div>
                        <Typography variant="button-field-info">Nurses: 37</Typography>
                        <Typography variant="button-field-info">Available: 16</Typography></div>
                </ButtonContainer>
                <ButtonContainer tabIndex={0} onClick={() => handleNavigate("/")}>
                    <Typography variant="button-field-header">Wards</Typography>
                    <img src="/wards.png" alt="wards image"/>
                    <div>
                        <Typography variant="button-field-info">Beds Total: 250</Typography>
                        <Typography variant="button-field-info">Occupied: 179</Typography></div>
                </ButtonContainer><StatisticsContainer tabIndex={0} onClick={() => handleNavigate("/")}><Typography
                variant="button-field-header">Statistics</Typography>
                <div>
                    <img src="/blockDiagram.png" alt="diagram image"/>
                    <img src="/arrowDiagram.png" alt="diagram image"/></div>
            </StatisticsContainer>
            </ButtonSectionContainer>
        </section>
    )
}