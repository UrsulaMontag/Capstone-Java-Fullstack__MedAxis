import * as ECT from '@whoicd/icd11ect';
import '@whoicd/icd11ect/style.css';

import {ISelectedEntity} from "../../models/icd_api/icdECTSelectedEntity.ts";
import Typography from "../../styles/Typography.tsx";
import {getAuthToken, searchIcd} from "../../services/IcdEctDataService.ts";
import {useParams} from "react-router-dom";
import {FC, useEffect} from "react";
import useHealthDataStore from "../../stores/useHealthDataStore.ts";

interface IcdECTProps {
    healthDataId?: string;
}

const IcdECT: FC<IcdECTProps> = ({healthDataId}) => {
    const iNo: number = 1;
    const addIcdCodeToPatient: (id: string, icdCode: string) => void = useHealthDataStore(state => state.addIcdCodeToPatientHealthData)

    const handleSelectedEntity = (selectedEntity: ISelectedEntity) => {
        console.log("Selected Entity: ", selectedEntity);
        const code = selectedEntity.code;

        if (healthDataId) {
            try {
                addIcdCodeToPatient(healthDataId, code);
                alert('ICD-11 code selected and saved: ' + code);
            } catch (error) {
                alert("Failed to save ICD-11 code. Please try again.");
            }
        } else {
            alert('ICD-11 code selected: ' + code);
        }
    };


    useEffect(() => {
        const fetchTokenAndConfigure = async () => {
            try {
                const token = await getAuthToken();
                console.log("Token fetched:", token);

                const settings = {
                    apiServerUrl: 'http://localhost:8088/api/icd/entity',
                    icdMinorVersion: 'v2',
                    icdLinearization: 'mms',
                    language: 'en',
                    flexisearchAvailable: true,
                    autoBind: false,
                    authHeaders: {
                        'Authorization': `Bearer ${token}`,
                        'Access-Control-Allow-Origin': "http://localhost:5173",
                    },
                    endpoints: {
                        search: '/mms/search'
                    }
                };
                const myCallbacks = {
                    selectedEntityFunction: handleSelectedEntity,
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
                            console.log("Words extracted:", words);
                            for (const word of words) {
                                console.log("Processing word:", word);
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
        <>
            <Typography variant="base">Start search:</Typography>
            <input
                type="text"
                className="ctw-input"
                autoComplete="off"
                data-ctw-ino={iNo}
            />
            <div className="ctw-window" data-ctw-ino={iNo}></div>
        </>
    );
};

const IcdECTWrapper: FC = () => {
    const {healthDataId} = useParams();
    return <IcdECT healthDataId={healthDataId}/>;
};

export {IcdECT, IcdECTWrapper};