import {createGlobalStyle} from "styled-components";

const GlobalStyle = createGlobalStyle`
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;

    }

    body {
        font-family: Roboto, sans-serif;
        font-size: 1.6rem;
        min-width: 320px;
        width: 100vw;
        min-height: 100vh;
        color: var(--color-dark);
        background-color: var(--color-light);

    }

    button {
        border-radius: 8px;
        border: 1px solid var(--color-dark);
        padding: .3rem .6rem;
        font-size: 1.4rem;
        cursor: pointer;
        transition: border-color 0.25s;
        background-color: var(--color-light);

    }

    button:hover {
        border-color: var(--accent-color-blue);
    }

    button:focus,
    button:focus-visible {
        outline: 4px auto -webkit-focus-ring-color;
    }

    @media only screen and (min-width: 768px) {
        body {
            display: flex;
            justify-content: center;
        }
    }


`;

export default GlobalStyle;
