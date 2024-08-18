import {useNavigate} from "react-router-dom";
import Button from "../styles/Button.styled.tsx";
import Login from "../components/base/Login.tsx";

export default function Dashboard() {
    const navigate = useNavigate();
    const handleNavigate = (path: string) => {
        navigate(path);
    }
    return (
        <>
            <Login/>
            <section>
                <Button variant="big" onClick={() => handleNavigate("/icd")}>ICD-API-Data</Button>
                <Button variant="big" onClick={() => handleNavigate("/patients")}>Patients</Button>
            </section>
        </>

    )
}