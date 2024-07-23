import {HeaderContainer, HeaderSection} from '../styles/Header.styled';
import NavBar from './NavBar';
import {useLocation, useNavigate} from "react-router-dom";
import Typography from "../styles/Typography.tsx";


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
                    <button type="button" onClick={onButtonClick}>Register patient</button>}
            </HeaderSection>

            <NavBar/>
        </HeaderContainer>
    );
}
