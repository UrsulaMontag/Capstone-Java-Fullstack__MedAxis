import {useState} from 'react';
import {HamburgerMenu, NavContainer, NavList, NavItem} from '../../styles/NavBar.styled.ts';
import {StyledLink} from "../../styles/Link.styled.ts";

export default function NavBar() {
    const [showNav, setShowNav] = useState(false);

    const toggleNav = () => {
        setShowNav(!showNav);
    };

    const handleLinkClick = () => {
        setShowNav(false);  // Ensure menu closes when a link is clicked
    };

    return (
        <>
            <NavContainer>
                <HamburgerMenu
                    $shownav={showNav}
                    onClick={toggleNav}
                >
                    <span></span>
                    <span></span>
                    <span></span>
                </HamburgerMenu>
                <NavList $shownav={showNav}>
                    <NavItem>
                        <StyledLink to="/" onClick={handleLinkClick}>Home</StyledLink>
                    </NavItem>
                    <NavItem>
                        <StyledLink to="/patients" onClick={handleLinkClick}>Patients</StyledLink>
                    </NavItem>
                </NavList>
            </NavContainer>
        </>
    );
}
