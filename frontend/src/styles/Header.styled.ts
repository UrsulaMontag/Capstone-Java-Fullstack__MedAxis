import styled from 'styled-components';

export const HeaderContainer = styled.header`
    position: sticky;
    top: 0;
    left: 0;
    display: flex;
    align-items: baseline;
    justify-content: space-between;
    gap: 1rem;
    padding: 1.6rem 0 1.6rem 1.6rem;
    background-color: var(--color-dark);
    width: 100vw;
    z-index: 3;
    @media (max-width: 760px) {
        padding-left: 0.2rem;
    }
`;
export const HeaderSection = styled.section`
    @media (min-width: 780px) {
        display: flex;
        gap: 4.8rem;
        align-items: center;
    }
`
