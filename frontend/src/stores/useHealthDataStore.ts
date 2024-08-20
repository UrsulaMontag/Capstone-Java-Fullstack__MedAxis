import {HealthData} from "../models/healt-data/HealthData.ts";
import {create} from "zustand";
import axios from "axios";

interface HealthDataState {
    healthData: HealthData;
    getHealthDataById: (id: string) => void;
    addIcdCodeToPatientHealthData: (id: string, icdCode: string) => void;
}

const useHealthDataStore = create<HealthDataState>()((set) => ({
    healthData: {id: "newId", icdCodes: []},
    getHealthDataById: async (id: string) => {

        try {
            const response = await axios.get(`/api/health_data/${id}`);
            const data = await response.data
            set({healthData: data});
        } catch (error) {
            console.error("Error getting health data of patient", error);
            alert(`Failed to get health data of patient. Please try again later.`);
        }
    },
    addIcdCodeToPatientHealthData: async (dataId: string, icdCode: string) => {
        try {
            const response = await axios.post(`/api/health_data/${dataId}/add-icd-code`, {icdCode});
            return response.data;
        } catch (error) {
            console.error("Error adding ICD code to patient", error);
            throw error;
        }
    }
}));
export default useHealthDataStore;

