import styled from "styled-components";

export const CardContainer = styled.article<{ details: boolean }>`
    width: ${props => props.details ? '90%' : '100%'};
    display: grid;
    grid-template-columns: ${props => props.details ? '1fr 3fr' : '10% 1fr 0.8fr 10%'};
    padding: ${props => props.details ? '1.6rem' : '1.2rem 1.2rem 1.2rem 0'};
    gap: ${props => props.details ? '1.2rem' : 'none'};

    border: 1px solid var(--accent-color-grey);
    border-radius: 8px;
    margin-bottom: .8rem;
    align-items: center;
    @media (max-width: 480px) {
        grid-template-columns: ${props => props.details ? '1fr 4fr' : '14% 0.8fr 0.8fr 10%'};
        padding: ${props => props.details ? '1rem' : '1.2rem 1.6rem .4rem .4rem'};
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
        gap: 2px;
    }
`
