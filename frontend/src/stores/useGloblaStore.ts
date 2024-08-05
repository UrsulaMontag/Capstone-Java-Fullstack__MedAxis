import {create} from "zustand";

interface GlobalState {
    searchTerm: string;
    setSearchTerm: (term: string) => void;
}

const useGlobalStore = create<GlobalState>()((set) => ({
    searchTerm: "",
    setSearchTerm: (term: string) => set({searchTerm: term}),
}));

export default useGlobalStore;