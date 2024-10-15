import { HealthData, MedicalExamination } from "../models/healt-data/HealthData.ts";
import { create } from "zustand";
import axios from "axios";

interface HealthDataState {
    healthData: HealthData;
    getHealthDataById: (id: string) => void;
    addExaminationWithIcdCodes: (id: string, examination: MedicalExamination) => void;
    updatePatientsHealthData: (id: string, medicalExaminations: MedicalExamination[]) => void;
}

const useHealthDataStore = create<HealthDataState>()((set) => ({
    healthData: {
        id: "newId",
        gender: "other",
        ageAtFirstAdmissionDate: Date.now(),
        medicalExaminations: []
    },
    getHealthDataById: async (id: string) => {

        try {
            const response = await axios.get(`/api/health_data/${id}`);
            const data = await response.data
            set({ healthData: data });
        } catch (error) {
            console.error("Error getting health data of patient", error);
            alert(`Failed to get health data of patient. Please try again later.`);
        }
    },
    addExaminationWithIcdCodes: async (dataId: string, examination: MedicalExamination) => {
        try {
            const response = await axios.post(`/api/health_data/${dataId}/add-examination`, examination);
            return response.data;
        } catch (error) {
            console.error("Error adding examination to patient", error);
            throw error;
        }
    },
    updatePatientsHealthData: async (dataId: string, medicalExaminations: MedicalExamination[]) => {
        try {
            const response = await axios.put(`/api/health_data/${dataId}/add-health-data`, medicalExaminations);
            return response.data;
        } catch (error) {
            console.error("Error updating patients health data", error);
            throw error;
        }
    }
}));
export default useHealthDataStore;

