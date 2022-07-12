import { Navigate } from "react-router-dom"
import About from "../page/About"
import Home from "../page/Home"
import NotFound from "../page/NotFound"

export default [
    {
        key: "Index",
        path: "/",
        component: <Navigate to="/home"/>,
    },
    {
        key: "Home",
        path: "/home",
        component: <Home/>,
    },
    {
        key: "About",
        path: "/about",
        component: <About/>,
    },
    {
        key: "NotFound",
        path: "*",
        component: <NotFound/>,
    },
]   