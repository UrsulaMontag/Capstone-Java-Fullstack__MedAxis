import {Patient, PatientToAdd} from "../models/Patient.ts";
import {create} from "zustand";
import axios from "axios";


interface PatientState {
    patients: Patient[];
    fetchPatients: () => void;
    getPatientById: (id: string) => void;
    createPatient: (newPatient: PatientToAdd) => void;
}

const usePatientStore = create<PatientState>()((set) => ({
    patients: [],
    fetchPatients: () => {
        axios.get<Patient[]>('/api/patients')
            .then(response => {
                set(({patients: response.data}))
            })
            .catch(error => console.error("Error getting patients", error))
    },
    getPatientById: (id: string) => {
        axios.get(`/api/patients/${id}`)
            .then(response => {
                set(state => ({
                    patients: [...state.patients, response.data]
                }))
            })
            .catch(error => console.error("Error getting patient by id: " + id, error))
    },
    createPatient: (newPatient: PatientToAdd) => {
        axios.post<Patient>('/api/patients/add', newPatient)
            .then(response => {
                set((state) => ({
                    patients: [...state.patients, response.data]
                }));
                //get().fetchPatients();
            })
    },
}));
export default usePatientStore;