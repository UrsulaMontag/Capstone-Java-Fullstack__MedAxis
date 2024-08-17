import useGlobalStore from "../../stores/useGloblaStore.ts";
import Button from "../../styles/Button.styled.tsx";
import {useNavigate} from "react-router-dom";

export default function Login() {
    const navigate = useNavigate();
    const userRole: "nurse" | "doctor" | null = useGlobalStore(state => state.userRole)
    const setUserRole = useGlobalStore(state => state.setUserRole)

    const handleLogin = (role: "nurse" | "doctor") => {
        setUserRole(role);
        alert("You're now logged in as " + role);
    };
    const handleLogout = () => {
        setUserRole(null);
        navigate("/");
        alert("You're logged out");

    }
    return (
        <>
            {userRole === null ? (
                <>
                    <Button variant="normal" onClick={() => handleLogin("nurse")}>Login as Nurse</Button>
                    <Button variant="normal" onClick={() => handleLogin("doctor")}>Login as Doctor</Button>
                </>
            ) : (
                <Button variant="normal" onClick={() => handleLogout()}>Logout</Button>
            )}</>
    )
}