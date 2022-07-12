import React from "react"
import ReactDOM from "react-dom/client"
import App from "./App"
import { RecoilRoot } from "recoil"
import { Provider } from "react-redux"
import {store} from "./config/ReduxConfig"

ReactDOM.createRoot(document.querySelector("#root") as Element)
.render(
    <React.StrictMode>
        <RecoilRoot>
            <App/>
        </RecoilRoot>
    </React.StrictMode>
)

// ReactDOM.createRoot(document.querySelector("#root") as Element)
// .render(
//     <React.StrictMode>
//         <Provider store={store}>
//             <App/>
//         </Provider>
//     </React.StrictMode>
// )