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
        case 'h4':
            return (
                <StyledH4 {...props.rest} as={props.component}>
                    {props.children}
                </StyledH4>
            );
        case 'info':
            return (
                <StyledInfoText {...props.rest} as={props.component}>
                    {props.children}
                </StyledInfoText>
            );
        case "error-info":
            return (
                <StyledErrorInfoText {...props.rest} as={props.component}>
                    {props.children}
                </StyledErrorInfoText>
            );
        case "auth-role":
            return (
                <StyledAuthRole {...props.rest} as={props.component}>
                    {props.children}
                </StyledAuthRole>
            )
        case "button-field-info":
            return (
                <StyledButtonFieldInfo {...props.rest} as={props.component}>
                    {props.children}
                </StyledButtonFieldInfo>
            )
        case "button-field-header":
            return (
                <StyledButtonFieldHeader {...props.rest} as={props.component}>
                    {props.children}
                </StyledButtonFieldHeader>
            )
        default:
            return <BaseText {...props.rest} as={props.component}>
                {props.children}
            </BaseText>
    }
}

const BaseText = styled.p`
    color: var(--color-dark);
    font-size: 1.4rem;
    font-weight: 400;
    @media (max-width: 760px) {
        font-size: 1.4rem;
    }
`
const StyledH1 = styled.h1`
    color: var(--accent-color-mainblue);
    font-size: 3.2rem;
    margin-top: 4.8rem;
    font-weight: 600;

    @media (min-width: 768px) {
        margin: 0;
    }
`;
const StyledH2 = styled.h2`
    color: var(--color-dark);
    font-size: 2.2rem;
    font-weight: 450;

`;
const StyledH3 = styled.h3`
    font-size: 1.4rem;
    font-weight: 400;
    @media (max-width: 760px) {
        font-size: 1.4rem;
    }
`;
const StyledH4 = styled.h4`
    font-size: 1.4rem;
    font-weight: 400;
    @media (max-width: 760px) {
        font-size: 1.4rem;
    }
`;
const StyledInfoText = styled.p`
    color: var(--accent-color-grey);
    font-weight: 200;
    font-size: 1.2rem;

`;
const StyledErrorInfoText = styled.span`
    color: var(--accent-color-red);
    background-color: var(--accent-color-red__transparent);
    border-radius: 5px;
    font-size: 1.2rem;
    font-weight: 400;
    line-height: 1.4rem;
    text-align: center
`;

const StyledAuthRole = styled(StyledInfoText)`
    color: var(--accent-color-mainblue);
    font-size: 1.2rem;
    font-weight: 300;
    place-self: center
`;

const StyledButtonFieldInfo = styled(BaseText)`
    font-size: 1.4rem;
    font-weight: 300;

`;
const StyledButtonFieldHeader = styled(StyledH3)`
    font-size: 1.6rem;
`;

