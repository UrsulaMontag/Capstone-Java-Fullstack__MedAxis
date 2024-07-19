import styled, {css} from 'styled-components';

export const NavContainer = styled.nav`
    display: flex;
    flex-direction: column;
    align-items: flex-start;

    @media (min-width: 768px) {
        flex-direction: row;
        align-items: center;
    }
`;

export const NavList = styled.ul<{ $shownav: boolean }>`
    z-index: 2;
    display: ${(props) => (props.$shownav ? 'flex' : 'none')};
    flex-direction: column;
    list-style-type: none;
    margin: 0 1.5rem 0 0.5rem;
    padding-top: 3rem;
    justify-content: flex-start;
    height: 100vh;
    width: 100vw;
    transition: transform 0.3s ease-in-out;
    transform: ${({$shownav}) => ($shownav ? 'translateX(0)' : 'translateX(-100%)')};

    @media (min-width: 768px) {
        display: flex;
        flex-direction: row;
        padding-top: 0;
        height: auto;
        width: auto;
        background-color: transparent;
        transform: none;
    }
`;

export const NavItem = styled.li`
    padding: 1rem;
`;

export const HamburgerMenu = styled.div<{ $shownav: boolean }>`
    @media (min-width: 780px) {
        display: none;
    }
    position: absolute;
    top: 1rem;
    right: 1rem;
    z-index: 300;
    cursor: pointer;
    color: antiquewhite;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: 28px;
    width: 28px;

    > span {
        display: block;
        height: 2px;
        width: 100%;
        background-color: black;
        border-radius: 9px;
        transition: all 0.4s ease-in-out;
    }

    ${props => props.$shownav && css`
        span:nth-child(1) {
            transform: translateY(14px) rotate(45deg);
        }

        span:nth-child(2) {
            opacity: 0;
        }

        span:nth-child(3) {
            transform: translateY(-12px) rotate(-45deg);
        }
    `}
`;
