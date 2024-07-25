import {createGlobalStyle} from "styled-components";

const GlobalStyle = createGlobalStyle`
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;

    }

    body {
        position: relative;
        font-family: Roboto, sans-serif;
        font-size: 1.6rem;
        min-width: 320px;
        width: 100vw;
        min-height: 100vh;
        color: var(--color-dark);
        background-color: var(--color-light);

    }
`;

export default GlobalStyle;
