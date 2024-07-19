import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Dashboard from "./routes/Dashboard.tsx";
import GlobalStyle from "./styles/GlobalStyle.styled.ts";
import CreatePatient from "./routes/CreatePatient.tsx";
import MainLayout from "./layouts/MainLayout.tsx";

function App() {
    const router = createBrowserRouter([
        {
            path: '/',
            element: <MainLayout/>,
            children:
                [
                    {
                        path: '/patients',
                        element: <Dashboard/>,
                    },
                    {
                        path: '/patients/add',
                        element: <CreatePatient/>,
                    }
                ]
        }
    ])

    return (
        <>
            <GlobalStyle/>
            <RouterProvider router={router}/>
        </>
    )
}

export default App
