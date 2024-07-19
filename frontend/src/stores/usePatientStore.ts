import {Patient, PatientToAdd} from "../models/Patient.ts";
import {create} from "zustand";
import axios from "axios";


interface PatientState {
    patients: Patient[];
    fetchPatients: () => void;
    createPatient: (newPatient: PatientToAdd) => void;
}

const usePatientStore = create<PatientState>()((set, get) => ({
    patients: [],
    fetchPatients: () => {
        axios.get<Patient[]>('/api/patients')
            .then(response => {
                set({patients: response.data})
            })
            .catch(error => console.error("Error fetching todos", error))
    },
    createPatient: (newPatient: PatientToAdd) => {
        axios.post<Patient>('/api/patients/add', newPatient)
            .then(response => {
                set((state) => ({
                    patients: [...state.patients, response.data]
                }));
                get().fetchPatients();
            })
    },
}));
export default usePatientStore;