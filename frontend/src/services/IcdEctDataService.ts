import axios from "axios";

const apiUrl = "/api/patients";
export const addIcdCodeToPatient = async (patientId: string, icdCode: string) => {
    try {
        const response = await axios.post(`${apiUrl}/${patientId}/add-icd-code`, {icdCode});
        return response.data;
    } catch (error) {
        console.error("Error adding ICD code to patient", error);
        throw error;
    }
}