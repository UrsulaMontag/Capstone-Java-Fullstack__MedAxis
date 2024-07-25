import {HeaderContainer, HeaderSection} from '../styles/Header.styled';
import NavBar from './NavBar';
import Typography from "../styles/Typography.tsx";
import PageTitle from "./PageTitle.tsx";

export default function Header() {

    return (
        <>
            <HeaderContainer>
                <HeaderSection>
                    <Typography variant="h1">MedAxis</Typography>
                </HeaderSection>
                <NavBar/>
            </HeaderContainer>
            <PageTitle/>
        </>
    );
}
