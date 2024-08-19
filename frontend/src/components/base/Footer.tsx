import {useLocation} from "react-router-dom";
import Typography from "../../styles/Typography.tsx";
import {FooterContainer, FooterSection} from "../../styles/Footer.styled.ts";
import useGlobalStore from "../../stores/useGloblaStore.ts";

export default function Footer() {
    const location = useLocation();
    const userRole: "nurse" | "doctor" | null = useGlobalStore(state => state.userRole)
    const isIcdToolView = location.pathname === "/icd";
    return (
        <FooterContainer>
            <FooterSection>
                {!isIcdToolView && <Typography variant="auth-role">Logged in as {userRole}</Typography>}
                {isIcdToolView && <Typography variant="info">WHO ICD-API v 2.4 - ECT v1.7.1</Typography>}
            </FooterSection>
        </FooterContainer>
    )
}