import RouterConfig from "./config/RouterConfig"
import {BrowserRouter} from "react-router-dom"

export default () => {

    return (
        <div>
            <BrowserRouter basename={process.env.PUBLIC_URL}>
                <RouterConfig/>
            </BrowserRouter>
        </div>
    )
}