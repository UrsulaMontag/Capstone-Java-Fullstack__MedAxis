import styled from 'styled-components';

export const FooterContainer = styled.footer`
    position: sticky;
    bottom: 0;
    height: 2rem;
    margin-top: 2px;

    display: flex;
    align-items: baseline;
    justify-content: space-between;
    gap: 1rem;
    padding: .8rem 0 .8rem 1.6rem;
    background-color: var(--color-dark);
    width: 100vw;
    z-index: 3;
    @media (max-width: 760px) {
        padding-left: 0.2rem;
    }
`;
export const FooterSection = styled.section`
    @media (min-width: 768px) {
        display: flex;
        gap: 4.8rem;
        align-items: center;
    }
`
