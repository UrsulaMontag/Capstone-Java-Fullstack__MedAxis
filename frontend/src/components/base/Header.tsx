import {HeaderContainer, HeaderSection} from '../../styles/Header.styled.ts';
import NavBar from './NavBar.tsx';
import Typography from "../../styles/Typography.tsx";
import PageTitle from "./PageTitle.tsx";
import SearchField from "./SearchField.tsx";
import {useLocation} from "react-router-dom";

export default function Header() {
    const location = useLocation();
    return (
        <>
            <HeaderContainer>
                <HeaderSection>
                    <Typography variant="h1">MedAxis</Typography>
                    {location.pathname === "/patients" &&
                        <SearchField/>}
                </HeaderSection>
                <NavBar/>
            </HeaderContainer>
            <PageTitle/>
        </>
    );
}
