import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Dashboard from "./routes/Dashboard.tsx";
import GlobalStyle from "./styles/GlobalStyle.styled.ts";
import WritePatient from "./routes/WritePatient.tsx";
import MainLayout from "./layouts/MainLayout.tsx";
import PatientList from "./routes/PatientList.tsx";
import PatientDetail from "./routes/PatientDetail.tsx";

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
                        element: <PatientDetail/>,
                    },
                    {
                        path: '/patients/add',
                        element: <WritePatient/>,
                    },
                    {
                        path: '/patients/edit/:id',
                        element: <WritePatient/>,
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
