import {useLocation, useNavigate, useParams} from "react-router-dom";
import Typography from "../../styles/Typography.tsx";
import {PageTitleContainer} from "../../styles/PageTitle.styled.ts";
import Button from "../../styles/Button.styled.tsx";

export default function PageTitle() {
    const location = useLocation();
    const urlParams = useParams();
    const navigate = useNavigate();

    const onButtonClick = () => {
        navigate("/patients/add");
    }
    const getPageTitle = () => {
        switch (location.pathname) {
            case "/":
                return "Dashboard";
            case "/patients":
                return "Patients";
            case "/patients/" + urlParams.id :
                return "Patient Details";
            case "/patients/add":
                return "Register Patient";
            case "/patients/edit/" + urlParams.id :
                return "Edit Patient";
            case "/icd":
                return "ICD-API Data - WHO";
            case "/health_data/" + urlParams.healthDataId:
                return "Patient Details";
            case "/health_data/" + urlParams.healthDataId + "/add-icd-details":
                return "ICD-API Data Diagnostics";
            default:
                return "Login first"
        }
    }

    return (
        <PageTitleContainer>
            <Typography variant="h2">{getPageTitle()}</Typography>
            {location.pathname === "/patients" &&
                <Button variant="normal" onClick={onButtonClick}><img alt="register-patient-button"
                                                                      src={"/add-user.png"}
                                                                      title="Add new patient"/>
                </Button>
            }
        </PageTitleContainer>
    )
}