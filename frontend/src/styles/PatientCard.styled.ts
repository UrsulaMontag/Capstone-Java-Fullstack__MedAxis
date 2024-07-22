import styled from "styled-components";
import {Link} from "react-router-dom";

export const CardContainer = styled.article`
    display: grid;
    grid-template-columns: 30vw 30vw 30vw;
    width: 90vw;

    border: 1px solid var(--color-dark);
    border-radius: 8px;
    padding: 2.4rem;
`;

export const CardActionContainer = styled.div`
    display: flex;
    justify-content: center;
    gap: 1.6rem;
`

export const CardActionLink = styled(Link)`
    border-radius: 8px;
    border: 1px solid var(--color-dark);
    padding: .3rem .6rem;
    font-size: 1.4rem;

    cursor: pointer;
    transition: border-color 0.25s;
    background-color: var(--color-light);
    text-decoration: none;
    color: var(--color-dark);
`;