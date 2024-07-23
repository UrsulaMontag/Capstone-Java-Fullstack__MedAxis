import styled from "styled-components";
import {Link} from "react-router-dom";


export const CardContainer = styled.article<{ details: boolean }>`
    width: ${props => props.details ? '90%' : '100%'};
    display: grid;
    grid-template-columns: ${props => props.details ? '1fr 3fr' : '10% 1fr 0.8fr 10%'};
    padding: 1.6rem 0;
    border: 1px solid var(--accent-color-grey);
    border-radius: 8px;
    margin-bottom: .8rem;
    align-items: center;
    @media (max-width: 480px) {
        grid-template-columns: ${props => props.details ? '1fr 3fr' : '14% 0.8fr 0.8fr 10%'};
    }
`;

export const NumberEntry = styled.p`
    justify-self: end;
    padding-right: 1.6rem;
    font-size: 1.6rem;
    font-weight: 550;
    @media (max-width: 480px) {
        font-size: 1.4rem;
    }
`


export const CardActionContainer = styled.div<{ details: boolean }>`
    display: flex;
    gap: 8px;
    justify-content: flex-end;
    align-items: center;
    grid-column: ${props => props.details ? 'span 2' : 'auto'};
    margin-top: ${props => props.details ? '16px' : '0'};
    @media (max-width: 600px) {
        gap: 2px
    }
`

export const CardActionLink = styled(Link)`
    border-radius: 8px;
    padding: .3rem .6rem;
    font-size: 1.4rem;
    width: fit-content;

    cursor: pointer;
    transition: border-color 0.25s;
    background-color: var(--accent-color-grey);
    text-decoration: none;
    color: var(--color-dark);
    @media (max-width: 480px) {
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