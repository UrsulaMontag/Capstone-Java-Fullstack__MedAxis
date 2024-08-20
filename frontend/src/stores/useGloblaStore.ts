import {create} from "zustand";

interface GlobalState {
    searchTerm: string;
    setSearchTerm: (term: string) => void;
    userRole: "nurse" | "doctor" | null;
    setUserRole: (role: "nurse" | "doctor" | null) => void;
}

const useGlobalStore = create<GlobalState>()((set) => ({
    searchTerm: "",
    setSearchTerm: (term: string) => set({searchTerm: term}),

    userRole: null,
    setUserRole: (role: "nurse" | "doctor" | null) => set({userRole: role})
}));

export default useGlobalStore;