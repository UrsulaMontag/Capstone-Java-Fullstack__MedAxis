import * as ECT from '@whoicd/icd11ect';
import '@whoicd/icd11ect/style.css';
import '../../styles/IcdECT.styled.css'
import {ISelectedEntity} from "../../models/icd_api/icdECTSelectedEntity.ts";
import Typography from "../../styles/Typography.tsx";
import {getAuthToken, searchIcd} from "../../services/IcdEctDataService.ts";
import {useEffect} from "react";

type IcdECTProps = {
    onEntitySelect: (entity: ISelectedEntity) => void;
    mode: "add" | "view";
}

export default function IcdECT(props: Readonly<IcdECTProps>) {
    const iNo: number = 1;
    const handleSelection = (selectedEntity: ISelectedEntity) => {
        props.mode === "add"
            ? props.onEntitySelect(selectedEntity)
            : alert(`Code: ${selectedEntity.code}\nDescription: ${selectedEntity.selectedText}`);
    };


    useEffect(() => {
        const fetchTokenAndConfigure = async () => {
            try {
                const token = await getAuthToken();
                const settings = {
                    apiServerUrl: 'http://localhost:8088/api/icd/entity',
                    icdMinorVersion: 'v2',
                    icdLinearization: 'mms',
                    language: 'en',
                    flexisearchAvailable: true,
                    autoBind: false,
                    enableKeyboard: true,
                    authHeaders: {
                        'Authorization': `Bearer ${token}`,
                        'Access-Control-Allow-Origin': "http://localhost:5173",
                    },
                    endpoints: {
                        search: '/mms/search'
                    }
                };
                const myCallbacks = {
                    selectedEntityFunction: handleSelection,
                    getNewTokenFunction: async () => {
                        await getAuthToken();
                    },
                    searchStartedFunction: () => {
                        //...
                    },
                    searchEndedFunction: async (searchResult: { searchResult: { words: string[]; } }) => {
                        console.log("Search result received:", searchResult);
                        if (searchResult && searchResult.searchResult) {
                            const words = searchResult.searchResult.words || [];
                            for (const word of words) {
                                await searchIcd(word);  // Jedes Wort einzeln an searchIcd übergeben
                            }
                        } else {
                            console.error("No search results found.");
                        }
                    }
                };

                ECT.Handler.configure(settings, {...myCallbacks});
                ECT.Handler.bind(iNo);
                console.log("ECT Handler configured and bound successfully");
            } catch (error) {
                console.error("Failed to fetch token or configure ECT", error);
            }
        };

        const configure = fetchTokenAndConfigure();

        return () => {
            configure.then(() => ECT.Handler.clear(iNo));
            // unbind to avoid memory leaks
        };
    }, [iNo]);

    return (
        <div className="ctw-container">
            <Typography variant="base">Start search:</Typography>
            <input
                type="text"
                className="ctw-input"
                autoComplete="off"
                data-ctw-ino={iNo}
            />
            <div className="ctw-window" data-ctw-ino={iNo}></div>
        </div>
    );
}
