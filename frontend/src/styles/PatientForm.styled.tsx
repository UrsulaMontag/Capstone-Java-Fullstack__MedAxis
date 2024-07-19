import styled from "styled-components";
import {FormEvent, JSX, ReactNode} from "react";

type PatientFormProps = {
    children: ReactNode,
    onSubmit: (event: FormEvent<HTMLFormElement>) => void,
    component?: keyof JSX.IntrinsicElements;
    rest?: { [key: string]: never },
}
export default function PatientFormStyled(props: Readonly<PatientFormProps>) {
    return (
        <PatientForm {...props.rest} as={props.component} onSubmit={props.onSubmit}>{props.children}</PatientForm>
    )
}

const PatientForm = styled.form`
    display: flex;
    flex-direction: column;
`;