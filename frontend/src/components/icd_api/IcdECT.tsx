import * as ECT from '@whoicd/icd11ect';
import '@whoicd/icd11ect/style.css';

import {ISelectedEntity} from "../../models/icd_api/icdECTSelectedEntity.ts";
import {Component} from "react";
import Typography from "../../styles/Typography.tsx";
import {addIcdCodeToPatient} from "../../services/IcdEctDataService.ts";
import {useParams} from "react-router-dom";

class IcdECT extends Component<{ patientId?: string }, any> {
    iNo = 1;

    constructor(props: any) {
        super(props);

        // configure the ECT
        const settings = {
            // the API located at the URL below should be used only for software development and testing
            apiServerUrl: 'https://icd11restapi-developer-test.azurewebsites.net',
            autoBind: false, // in React we recommend using the manual binding
        };
        const callbacks = {
            selectedEntityFunction: this.handleSelectedEntity.bind(this),
        };
        ECT.Handler.configure(settings, callbacks);
    }

    componentDidMount() {
        // manual binding only after the component has been mounted
        ECT.Handler.bind(this.iNo);
    }

    async handleSelectedEntity(selectedEntity: ISelectedEntity) {
        if (this.props.patientId) {
            try {
                const {patientId} = this.props;
                await addIcdCodeToPatient(patientId, selectedEntity.code);
                alert('ICD-11 code selected and saved: ' + selectedEntity.code);
            } catch (error) {
                alert("Failed to save ICD-11 code. Please try again.")
            }
        } else {
            alert('ICD-11 code selected: ' + selectedEntity.code);
        }
    }

    render() {
        return (
            <>
                <Typography variant="base">Start search:</Typography>
                {/* input element used for typing the search */}
                <input
                    type="text"
                    className="ctw-input"
                    autoComplete="off"
                    data-ctw-ino={this.iNo}
                />
                {/* div element used for showing the search results */}
                <div className="ctw-window" data-ctw-ino={this.iNo}></div>
            </>
        );
    }
}

const IcdECTWrapper = () => {
    const {patientId} = useParams();
    return <IcdECT patientId={patientId}/>;
};

export {IcdECT, IcdECTWrapper};