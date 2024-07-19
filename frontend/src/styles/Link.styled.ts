import styled from "styled-components";
import {Link} from "react-router-dom";

export const StyledLink = styled(Link)`
    color: var(--accent-color-light);
    text-decoration: none;
    cursor: pointer;
    line-height: 3rem;

    &:hover {
        color: var(--accent-color-blue);
        font-weight: 550;
    }

    &:active,
    &:focus {
        color: var(--accent-color-mainblue);
    }
`