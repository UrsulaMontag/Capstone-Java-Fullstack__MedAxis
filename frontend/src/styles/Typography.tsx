import styled from 'styled-components';
import {JSX, ReactNode} from "react";

type TypographyProps = {
    children: ReactNode,
    variant: string,
    component?: keyof JSX.IntrinsicElements,
    rest?: { [key: string]: never }
}
export default function Typography(props: Readonly<TypographyProps>) {
    switch (props.variant) {
        case 'base':
            return (
                <BaseText{...props.rest} as={props.component}>
                    {props.children}</BaseText>
            );
        case 'h1':
            return (
                <StyledH1 {...props.rest} as={props.component}>
                    {props.children}
                </StyledH1>
            );
        case 'h2':
            return (
                <StyledH2 {...props.rest} as={props.component}>
                    {props.children}
                </StyledH2>
            );
        case 'h3':
            return (
                <StyledH3 {...props.rest} as={props.component}>
                    {props.children}
                </StyledH3>
            );
        case 'info':
            return (
                <StyledInfoText {...props.rest} as={props.component}>
                    {props.children}
                </StyledInfoText>
            );
    }
}

const BaseText = styled.p`
    color: var(--color-dark);
    font-size: 1.6rem;
    font-weight: 400;
    @media (max-width: 480px) {
        font-size: 1.4rem;
    }
`
const StyledH1 = styled.h1`
    color: var(--accent-color-mainblue);
    font-size: 2.8rem;
    margin-top: 4.8rem;
    @media (min-width: 768px) {
        margin: 0;
    }
`;
const StyledH2 = styled.h2`
    color: var(--color-dark);
    font-size: 2.2rem;
`;
const StyledH3 = styled.h3`
    font-size: 1.6rem;
    font-weight: 550;
    @media (max-width: 480px) {
        font-size: 1.4rem;
    }
`;
const StyledInfoText = styled.p`
    color: var(--accent-color-grey);
    font-weight: 400;
`;
