import * as ECT from '@whoicd/icd11ect';
import '@whoicd/icd11ect/style.css';

import {ISelectedEntity} from "../../models/icd_api/icdECTSelectedEntity.ts";
import {Component} from "react";
import Typography from "../../styles/Typography.tsx";

class IcdECT extends Component<any, any> {
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
            selectedEntityFunction: (selectedEntity: ISelectedEntity) => {
                // shows an alert with the code selected by the user and then clears the search results
                alert('ICD-11 code selected: ' + selectedEntity.code);
                ECT.Handler.clear(this.iNo);
            },
        };
        ECT.Handler.configure(settings, callbacks);
    }

    componentDidMount() {
        console.log('componentDidMount()');
        // manual binding only after the component has been mounted
        ECT.Handler.bind(this.iNo);
    }

    render() {
        return (
            <>
                <Typography variant="base">Start search:</Typography>
                {/* input element used for typing the search */}
                <input
                    type="text"
                    className="ctw-input"
                    autoComplete="on"
                    data-ctw-ino={this.iNo}
                />
                {/* div element used for showing the search results */}
                <div className="ctw-window" data-ctw-ino={this.iNo}></div>
            </>
        );
    }
}

export default IcdECT;