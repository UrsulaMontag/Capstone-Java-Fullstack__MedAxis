import styled from "styled-components";

export const PatientListContainer = styled.ul`
    width: 100vw;
    position: relative;

`;

export const ListHeader = styled.div`
    position: sticky;
    top: 12.2rem;
    left: 0;
    display: grid;
    grid-template-columns: 10% 1fr 0.8fr 10%;
    padding: 0.4rem 1.2rem 0.4rem 0;
    margin-bottom: 1.6rem;
    border-bottom: 1px solid var(--color-dark);
    z-index: 1;
    @media (max-width: 760px) {
        grid-template-columns: 14% 0.8fr 0.8fr 10%;
        top: 16rem;
        margin-bottom: .6rem;
        padding: 1.2rem 1.2rem .4rem 0.4rem;
    }
    background-color: var(--accent-color-grey);
`;

export const PatientListItem = styled.li`
    list-style: none;
    z-index: 0;
`;