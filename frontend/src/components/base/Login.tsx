import useGlobalStore from "../../stores/useGloblaStore.ts";
import Button from "../../styles/Button.styled.tsx";
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {StyledSelect} from "../../styles/Login.styled.ts";

export default function Login() {
    const navigate = useNavigate();
    const userRole: "nurse" | "doctor" | null = useGlobalStore(state => state.userRole)
    const setUserRole = useGlobalStore(state => state.setUserRole);
    const [selectedRole, setSelectedRole] = useState<"nurse" | "doctor" | "">("");

    const handleRoleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const role = event.target.value as "nurse" | "doctor";
        setSelectedRole(role);
        if (role) {
            handleLogin(role);
        }
    };

    const handleLogin = (role: "nurse" | "doctor") => {
        setUserRole(role);
        alert("You're now logged in as " + role);
    };

    const handleLogout = () => {
        setUserRole(null);
        setSelectedRole("");
        navigate("/");
        alert("You're logged out");
    };

    return (
        <>
            {userRole === null ? (
                <StyledSelect value={selectedRole} onChange={handleRoleChange}>
                    <option value="" disabled>Login to use MedAxis</option>
                    <option value="nurse">Nurse</option>
                    <option value="doctor">Doctor</option>
                </StyledSelect>
            ) : (
                <Button variant="normal" onClick={handleLogout}>Logout</Button>
            )}
        </>
    );
}