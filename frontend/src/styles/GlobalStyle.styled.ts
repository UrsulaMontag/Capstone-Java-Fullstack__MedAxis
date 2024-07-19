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
    
    @media only screen and (min-width: 768px) {
        body {
            display: flex;
            justify-content: center;
        }
    }


`;

export default GlobalStyle;
