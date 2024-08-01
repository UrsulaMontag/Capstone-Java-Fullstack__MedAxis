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
    display: grid;
    grid-template-columns: 1fr 3fr;
    gap: 1.2rem;
    align-items: center;
    font-size: 1.4rem;
    @media (max-width: 480px) {
        margin-bottom: .6rem;
        padding-left: 0.2rem;
    }

    & label {
        grid-column: 1;
    }

    & input {
        grid-column: 2;
        height: 1.6rem;
        font-size: 1.4rem;

        border: none;
        border-bottom: 1px solid var(--accent-color-grey);
        cursor: pointer;
    }

    & div {
        grid-column: 2;
        display: flex;
        justify-content: flex-end;
        gap: 1.6rem;
        @media (max-width: 600px) {
            gap: 2px;
            padding-right: 3.6rem;
        }
    }
`;