import {Patient} from "../../models/patient/Patient.ts";
import {HealthData} from "../../models/healt-data/HealthData.ts";
import Typography from "../../styles/Typography.tsx";
import {formatDate} from "../../utils/formatDateView.ts";
import useHealthDataStore from "../../stores/useHealthDataStore.ts";
import {useEffect, useState} from "react";

type PatientHealthCardProps = {
    patient: Patient;
}

export default function PatientHealthCard(props: Readonly<PatientHealthCardProps>) {
    const patient = props.patient;
    const healthData: HealthData = useHealthDataStore(state => state.healthData)
    const getPatientsHealthData = useHealthDataStore(state => state.getHealthDataById);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                getPatientsHealthData(patient.healthDataId);
                setLoading(false);
            } catch (err) {
                console.error("Error fetching health data:", err);
                setError("Failed to load health data.");
                setLoading(false);
            }
        };

        fetchData();
    }, [patient.healthDataId]);

    if (loading) {
        return <Typography variant="base">Loading health data...</Typography>;
    }

    if (error) {
        return <Typography variant="base">{error}</Typography>;
    }

    if (!healthData) {
        return <Typography variant="base">Health data not found.</Typography>;
    }

    const icdCodesArray: string[] = healthData.icdCodes;
    const extractIcdCodesFromResponse = (response: string): { code: string, description: string }[] => {
        const jsonLikeObjects = response.split('},{').map((item, index, array) => {
            if (index !== 0) item = '{' + item;
            if (index !== array.length - 1) item = item + '}';
            return item;
        });

        return jsonLikeObjects.map(jsonString => {
            const parsedObject = JSON.parse(jsonString);
            const [code, ...descriptionParts] = parsedObject.icdCode.split(":");
            const description = descriptionParts.join(":").trim(); // Handle cases where the description might contain ':'

            return {
                code: code.trim(),
                description
            }
        });
    }


    return (

        <>
            <Typography variant="h3">Name: </Typography>
            <Typography variant="base">{patient.lastname} {patient.firstname}</Typography>
            <Typography variant="h3">Birthdate: </Typography>
            <Typography variant="base">{formatDate(patient.dateOfBirth)}</Typography>
            <Typography variant="h3">Health Data:</Typography>
            {icdCodesArray.map(icdCode => (<><Typography
                variant="base">{extractIcdCodesFromResponse(icdCode)[0].code}:</Typography><br/><Typography
                variant="info">{extractIcdCodesFromResponse(icdCode)[0].description}</Typography><br/></>))}

        </>
    );
}