import { Route, Routes } from "react-router-dom"
import RouteList from "../router/index"
import React from "react"

interface AppRoute {
    key: string,
    path: string,
    children: Array<AppRoute>,
    component: React.ReactNode,
}

export default () => {

    const renderRoutes = (function recursion(routes: Array<AppRoute>, key: String) {
        return routes.map(r => {
            if (r.children) {
                return (
                    <Route key={key + r.path} path={r.path} element={r.component}>
                        {
                            recursion(r.children, key + r.path)
                        }
                    </Route>
                )
            } else {
                return (
                    <Route key={key + r.path} path={r.path} element={r.component} />
                )
            }
        })
    })

    return (
        <Routes>
            {
                renderRoutes(RouteList as Array<AppRoute>, "")
            }
        </Routes>
    )
}