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
    height: 36vh;
    background-color: var(--accent-color-grey__light);
    border-radius: 5px;
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
    padding: 0 1.6rem 1.6rem 1.6rem;


    cursor: pointer;
    transition: border-color 0.25s;
    @media (max-width: 760px) {
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

    img {
        width: 10rem;
        align-self: flex-end;
        position: absolute;
        top: 1.6rem;
        background-color: var(--accent-color-grey__light);
        @media (max-width: 760px) {
            width: 6rem;
        }
    }
`;

export const StatisticsContainer = styled(ButtonContainer)`
    width: 86vw;
    flex-direction: row;
    justify-content: space-between;

    h3 {
        align-self: center;
    }

    div {
        display: flex;
        gap: 1.6rem;
    }

    img {
        position: unset;
        width: 15rem;
    }
`;