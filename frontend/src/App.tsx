import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Dashboard from "./routes/Dashboard.tsx";
import GlobalStyle from "./styles/GlobalStyle.styled.ts";
import WritePatient from "./routes/WritePatient.tsx";
import MainLayout from "./layouts/MainLayout.tsx";
import PatientList from "./routes/PatientList.tsx";
import PatientDetail from "./routes/PatientDetail.tsx";
import {IcdECT, IcdECTWrapper} from "./components/icd_api/IcdECT.tsx";
import ProtectedRoute from "./routes/routeSec/ProtectedRoute.tsx";
import useGlobalStore from "./stores/useGloblaStore.ts";

function App() {

    const userRole: "nurse" | "doctor" | null = useGlobalStore(state => state.userRole)
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
                        children: []
                    },
                    {
                        path: '/patients/:id',
                        element:
                            <ProtectedRoute role={userRole} allowedRoles={["nurse", "doctor"]}>
                                <PatientDetail/>
                            </ProtectedRoute>

                    },
                    {
                        path: '/patients/add',
                        element:
                            <ProtectedRoute role={userRole} allowedRoles={["nurse"]}>
                                <WritePatient/>
                            </ProtectedRoute>,
                    },
                    {
                        path: '/patients/edit/:id',
                        element: <ProtectedRoute role={userRole} allowedRoles={["nurse"]}>
                            <WritePatient/>
                        </ProtectedRoute>,
                    },

                    {
                        path: '/icd',
                        element: <ProtectedRoute role={userRole} allowedRoles={["nurse", "doctor"]}>
                            <IcdECT/>
                        </ProtectedRoute>,
                    },

                    {
                        path: '/health_data/:healthDataId/add-icd-details',
                        element: <ProtectedRoute role={userRole} allowedRoles={["doctor"]}>
                            <IcdECTWrapper/>
                        </ProtectedRoute>,
                    },

                    {
                        path: '/health_data/:healthDataId',
                        element: <ProtectedRoute role={userRole} allowedRoles={["doctor"]}>
                            <PatientDetail/>
                        </ProtectedRoute>,
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
