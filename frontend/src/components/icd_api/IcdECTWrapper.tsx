import {ISelectedEntity} from "../../models/icd_api/icdECTSelectedEntity.ts";
import IcdECT from "./IcdECT"; // Import the IcdECT component

type IcdECTWrapperProps = {
    onSelect: (icdCode: { code: string; description: string }) => void;
}

export default function IcdECTWrapper(props: Readonly<IcdECTWrapperProps>) {
    const handleSelectedEntity = (selectedEntity: ISelectedEntity) => {
        const code = selectedEntity.code;
        const title = selectedEntity.selectedText;

        props.onSelect({code, description: title});
    };

    return (
        <div>
            <IcdECT onEntitySelect={handleSelectedEntity} mode="add"/>
        </div>
    );
}

