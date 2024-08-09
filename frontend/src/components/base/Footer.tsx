import {useLocation} from "react-router-dom";
import Typography from "../../styles/Typography.tsx";
import {FooterContainer, FooterSection} from "../../styles/Footer.styled.ts";

export default function Footer() {
    const location = useLocation();
    return (
        <FooterContainer>
            <FooterSection>
                {location.pathname === "/icd" && <Typography variant="info">WHO ICD-API v 2.4 - ECT v1.7.1</Typography>}
            </FooterSection>
        </FooterContainer>
    )
}