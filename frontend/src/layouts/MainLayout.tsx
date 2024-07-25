import {Outlet} from "react-router-dom";
import Header from "../components/base/Header.tsx";
import {MainContent} from "../styles/MainContent.ts";

export default function MainLayout() {
    return (
        <>
            <Header/>
            <MainContent>
                <Outlet/>
            </MainContent>
        </>
    )
}