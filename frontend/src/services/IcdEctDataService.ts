import axios from "axios";

const apiUrl = "/api/health_data";
export const addIcdCodeToPatient = async (dataId: string, icdCode: string) => {
    try {
        const response = await axios.post(`${apiUrl}/${dataId}/add-health-data`, {icdCode});
        return response.data;
    } catch (error) {
        console.error("Error adding ICD code to patient", error);
        throw error;
    }
}

export const getAuthToken = async (): Promise<string> => {
    try {
        const response = await axios.get('http://localhost:8088/api/icd/entity/icd/release/11/mms/token');
        return response.data;
    } catch (error) {
        console.error("Error fetching auth token:", error);
        throw error;
    }
}

export const searchIcd = async (word: string) => {
    try {
        const token = await getAuthToken();

        // Parameter für die URL
        const params = new URLSearchParams({
            q: word,
            subtreeFilterUsesFoundationDescendants: 'false',
            includeKeywordResult: 'false',
            useFlexisearch: 'true',
            flatResults: 'true',
            highlightingEnabled: 'true',
            medicalCodingMode: 'true'
        });

        const response = await axios.post(
            `http://localhost:8088/api/icd/entity/release/11/v2/mms/search?${params.toString()}`,
            {},
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                }
            }
        );

        if (response.status === 200) {
            return response.data;
        }
    } catch (error) {
        console.error('Error during search:', error);
        throw error;
    }


};