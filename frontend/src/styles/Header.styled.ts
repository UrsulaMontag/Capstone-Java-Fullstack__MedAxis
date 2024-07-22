import styled from 'styled-components';

export const HeaderContainer = styled.header`
    position: sticky;
    top: 0;
    left: 0;
    display: flex;
    align-items: center;
    gap: 1rem;
    padding: 1rem;
    background-color: var(--color-dark);
    width: 100vw;
    margin-bottom: 3.6rem;


    @media (min-width: 768px) {
        justify-content: space-between;
    }
`;
export const HeaderSection = styled.section`
    @media (min-width: 780px) {
        display: flex;
        gap: 4.8rem;
        align-items: baseline;
    }
`
