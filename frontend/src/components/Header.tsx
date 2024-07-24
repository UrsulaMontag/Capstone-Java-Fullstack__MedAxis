import {HeaderContainer, HeaderSection} from '../styles/Header.styled';
import NavBar from './NavBar';
import {useLocation, useNavigate} from "react-router-dom";
import Typography from "../styles/Typography.tsx";
import Button from "../styles/Button.styled.tsx";


export default function Header() {
    const navigate = useNavigate();
    const location = useLocation();
    const onButtonClick = () => {
        navigate("/patients/add");
    }


    return (
        <HeaderContainer>
            <HeaderSection>
                <Typography variant="h1">MedAxis</Typography>
                {location.pathname !== "/patients/add" &&
                    <Button variant="normal" onClick={onButtonClick}>Register patient</Button>}
            </HeaderSection>

            <NavBar/>
        </HeaderContainer>
    );
}
