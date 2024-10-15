import {ISelectedEntity} from "../../models/icd_api/icdECTSelectedEntity.ts";
import IcdECT from "./IcdECT.tsx";

export default function IcdECTInfo() {
    const handleEntitySelect = (entity: ISelectedEntity) => {
        console.log(`Selected Entity: ${entity}`);
    }

    return (
        <IcdECT onEntitySelect={handleEntitySelect} mode="view"/>
    )
}