import {FormEvent, JSX, ReactNode} from "react";
import styled from "styled-components";

type ButtonProps = {
    children?: ReactNode;
    variant: string;
    component?: keyof JSX.IntrinsicElements;
    rest?: { [p: string]: never };
    onClick?: (event: FormEvent<HTMLButtonElement>) => void;
}

export default function Button(props: Readonly<ButtonProps>) {
    switch (props.variant) {
        case "normal":
            return (
                <BaseButton as={props.component} {...props.rest} onClick={props.onClick}>
                    {props.children}
                </BaseButton>
            )
        case "ok":
            return (
                <OkButton{...props.rest} as={props.component} onClick={props.onClick}>
                    {props.children}
                </OkButton>
            )
        case "delete":
            return (
                <DeleteButton {...props.rest} as={props.component} onClick={props.onClick}>
                    {props.children}
                </DeleteButton>
            )
        case "big":
            return (
                <BigButton {...props.rest} as={props.component} onClick={props.onClick}>
                    {props.children}
                </BigButton>
            )
        default:
            return null;
    }
}

const BaseButton = styled.button`
    border-radius: 5px;
    padding: .3rem .6rem;
    font-size: 1.4rem;
    width: fit-content;

    cursor: pointer;
    transition: border-color 0.25s;
    background-color: var(--accent-color-grey);
    text-decoration: none;
    color: var(--color-dark);
    @media (max-width: 760px) {
        font-size: 1.2rem;
        padding: .1rem .3rem;

    }

    &:hover {
        border-color: var(--accent-color-blue);
    }

    &:focus,
    &:focus-visible {
        outline: 4px auto -webkit-focus-ring-color;
    }
`;
const OkButton = styled(BaseButton)`
    border: 2px solid var(--accent-color-green);
`;
const DeleteButton = styled(BaseButton)`
    border: 2px solid var(--accent-color-red);
`;
const BigButton = styled(BaseButton)`
    min-width: 40vw;
    height: fit-content;
`;
