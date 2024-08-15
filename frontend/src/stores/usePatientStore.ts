import {Patient, PatientToAdd} from "../models/patient/Patient.ts";
import {create} from "zustand";
import axios from "axios";


interface PatientState {
    patients: Patient[];
    fetchPatients: () => void;
    getPatientById: (id: string) => void;
    createPatient: (newPatient: PatientToAdd) => void;
    updatePatient: (id: string, newPatient: PatientToAdd) => void;
    deletePatient: (id: string) => void;
}

const usePatientStore = create<PatientState>()((set) => ({
    patients: [],
    fetchPatients: () => {
        axios.get<Patient[]>('/api/patients')
            .then(response => {
                console.log("Fetched patients: ", response.data);
                set(({patients: response.data}))
            })
            .catch(error => {
                console.error("Error getting patients", error);
                alert("Failed to fetch patients. Please try again later.");
            });
    },
    getPatientById: (id: string) => {
        axios.get(`/api/patients/${id}`)
            .then(response => {
                set(state => ({
                    patients: [...state.patients, response.data]
                }))
            })
            .catch(error => {
                console.error("Error getting patient by id: " + id, error);
                alert(`Failed to get patient with id ${id}. Please try again later.`);
            });
    },
    createPatient: (newPatient: PatientToAdd) => {
        axios.post<Patient>('/api/patients/add', newPatient)
            .then(response => {
                set((state) => ({
                    patients: [...state.patients, response.data]
                }));
                alert("Patient added successfully.");
            })
            .catch(error => {
                console.error("Error creating patient", error);
                alert("Failed to add patient. Please check your input and try again.");
            });
    },
    updatePatient: (id: string, newPatient: PatientToAdd) => {
        axios.put(`/api/patients/edit/${id}`, newPatient)
            .then(response => {
                set((state) => ({
                    patients: state.patients.map(patient => patient.id === id ? response.data : patient)
                }));
                alert("Patient updated successfully.");
            })
            .catch(error => {
                console.error("Error updating patient", error);
                alert("Failed to update patient. Please check your input and try again.");
            });
    },
    deletePatient: (id: string) => {
        axios.delete(`/api/patients/${id}`)
            .then(() => {
                set(state => ({
                    patients: state.patients.filter(patient => patient.id !== id)
                }));
                alert("Patient deleted successfully.");
            })
            .catch(error => {
                console.error("Error deleting patient", error);
                alert("Failed to delete patient. Please try again later.");
            });
    }
}));
export default usePatientStore;