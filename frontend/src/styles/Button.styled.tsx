import {JSX, ReactNode} from "react";
import styled from "styled-components";

type ButtonProps = {
    children?: ReactNode;
    variant: string;
    component?: keyof JSX.IntrinsicElements;
    rest?: { [p: string]: never };
    onClick?: () => void;
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
                <OkButton{...props.rest} as={props.component}>
                    {props.children}
                </OkButton>
            )
        case "delete":
            return (
                <DeleteButton {...props.rest} as={props.component}>
                    {props.children}
                </DeleteButton>
            )
        default:
            return null;
    }
}

const BaseButton = styled.button`
    button {
        border-radius: 8px;
        border: 1px solid var(--color-dark);
        padding: .3rem .6rem;
        font-size: 1.4rem;
        cursor: pointer;
        transition: border-color 0.25s;
        background-color: var(--color-light);

    }

    button:hover {
        border-color: var(--accent-color-blue);
    }

    button:focus,
    button:focus-visible {
        outline: 4px auto -webkit-focus-ring-color;
    }
`;
const OkButton = styled(BaseButton)`
    color: var(--accent-color-green);
`;
const DeleteButton = styled(BaseButton)`
    color: var(--accent-color-red);
`;