import {ReactNode} from "react";
import {Navigate} from "react-router-dom";

type ProtectedRouteProps = {
    children: ReactNode,
    role: string | null,
    allowedRoles: string[]
}

export default function ProtectedRoute(props: Readonly<ProtectedRouteProps>) {
    if (!props.role || !props.allowedRoles.includes(props.role)) {
        alert("Please login before usage")
        return <Navigate to={"/"}/>
    }
    return props.children;
}