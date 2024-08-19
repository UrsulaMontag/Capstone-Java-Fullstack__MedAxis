import styled from "styled-components";

export const StyledSelect = styled.select`
    border-radius: 5px;
    padding: .3rem .6rem;
    font-size: 1.4rem;
    width: fit-content;

    cursor: pointer;
    transition: border-color 0.25s;
    background-color: var(--accent-color-grey);
    text-decoration: none;
    color: var(--color-dark);
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
`;