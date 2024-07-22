import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Dashboard from "./routes/Dashboard.tsx";
import GlobalStyle from "./styles/GlobalStyle.styled.ts";
import CreatePatient from "./routes/CreatePatient.tsx";
import MainLayout from "./layouts/MainLayout.tsx";
import PatientList from "./routes/PatientList.tsx";

function App() {
    const router = createBrowserRouter([
        {
            path: '/',
            element: <MainLayout/>,
            children:
                [
                    {
                        path: '/',
                        element: <Dashboard/>,
                    },
                    {
                        path: '/patients',
                        element: <PatientList/>,
                    },
                    {
                        path: '/patients/:id',
                        element: <CreatePatient/>,
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
