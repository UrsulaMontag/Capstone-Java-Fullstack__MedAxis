import styled from "styled-components";

export const PageTitleContainer = styled.div`
    position: sticky;
    background-color: var(--accent-color-mainblue);
    margin-bottom: 1.6rem;
    padding: 1.2rem 1.2rem 1.4rem 1.6rem;
    z-index: 2;
    top: 6.4rem;
    display: flex;
    justify-content: space-between;
    @media (max-width: 480px) {
        margin-bottom: .6rem;
        padding-left: 0.2rem;
        top: 10.8rem;
    }

`;