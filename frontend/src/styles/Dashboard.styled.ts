import styled from "styled-components";

export const ButtonSectionContainer = styled.article`
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-items: center;
    gap: 1.6rem;
`;
export const ButtonContainer = styled.div`
    position: relative;
    width: 42vw;
    height: 32vh;
    background-color: var(--accent-color-grey__light);
    border-radius: 5px;
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
    padding-left: 1.6rem;

    img {
        width: 10rem;
        align-self: flex-end;
        position: absolute;
        top: 0;
        background-color: var(--accent-color-grey__light);
        @media (max-width: 760px) {
            width: 6rem;
        }
    }
`;

export const StatisticsContainer = styled(ButtonContainer)`
    width: 86vw;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;


    div {
        display: flex;
        gap: 1.6rem;
    }

    img {
        position: unset;
        width: 15rem;
    }
`;