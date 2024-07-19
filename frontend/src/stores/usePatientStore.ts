import {Patient} from "../models/Patient.ts";
import {createStore} from "zustand";
import axios from "axios";


interface PatientState {
    patients: Patient[];
    fetchPatients: () => void;
    createPatient: (patient: Patient) => void;
}

const usePatientStore = createStore<PatientState>()((set, get) => ({
    patients: [],
    fetchPatients: async () => {
        try {
            const response = await axios.get<Patient[]>('/api/patients');
            set({patients: response.data});
        } catch (error) {
            console.error('Error fetching patients', error);
        }
    },
    createPatient: async (newPatient: Patient) => {
        try {
            const response = await axios.post<Patient>('/api/patients/add', {
                firstname: newPatient.firstname,
                lastname: newPatient.lastname,
                dateOfBirth: newPatient.dateOfBirth
            });
            set((state) => ({
                patients: [...state.patients, response.data]
            }));
            get().fetchPatients();
        } catch (error) {
            console.error('Error creating new patient', error);
        }
    },
}));
export default usePatientStore;