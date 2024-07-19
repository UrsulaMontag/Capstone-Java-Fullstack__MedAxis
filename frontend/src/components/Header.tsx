import {HeaderContainer, HeaderSection} from '../styles/Header.styled';
import NavBar from './NavBar';
import {useLocation, useNavigate} from "react-router-dom";


export default function Header() {
    const navigate = useNavigate();
    const location = useLocation();
    const onButtonClick = () => {
        navigate("/patients/add");
    }


    return (
        <HeaderContainer>
            <HeaderSection>
                <h1>MedAxis</h1>
                {location.pathname !== "/patients/add" &&
                    <button type="button" onClick={onButtonClick}>Register patient</button>}
            </HeaderSection>

            <NavBar/>
        </HeaderContainer>
    );
}
