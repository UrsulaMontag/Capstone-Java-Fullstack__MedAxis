import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Dashboard from "./routes/Dashboard.tsx";
import GlobalStyle from "./styles/GlobalStyle.styled.ts";
import WritePatient from "./routes/WritePatient.tsx";
import MainLayout from "./layouts/MainLayout.tsx";
import PatientList from "./routes/PatientList.tsx";
import PatientDetail from "./routes/PatientDetail.tsx";
import ProtectedRoute from "./routes/routeSec/ProtectedRoute.tsx";
import useGlobalStore from "./stores/useGloblaStore.ts";
import HealthDataForm from "./components/health-data/HealthDataForm.tsx";
import {IcdECT} from "./components/icd_api/IcdECT.tsx";

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

                        element: <ProtectedRoute role={userRole} allowedRoles={["nurse", "doctor"]}>
                            <PatientList/>
                        </ProtectedRoute>
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
                            <HealthDataForm/>
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
