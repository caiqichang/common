import {DateTime} from "luxon"
import { useNavigate } from "react-router-dom"
import UserStore from "../store/UserStore"
import BookStore from "../store/BookStore"
import Comp1 from "./Comp1"

export default () => {

    const history = useNavigate()

    const userStore = UserStore()
    const bookStore = BookStore()


    const doClick = () => {
        let i = 0
        i += 5
        console.log(i)
    }

    return (
        <div>
            <div>Home</div>
            <div>{ DateTime.now().toFormat("yyyy-MM-dd") }</div>
            <div>
            <button onClick={doClick}>Test</button>
            </div>
            <div>
                <button onClick={() => {
                    history("/about")
                }}>to About</button>
            </div>
            <div>
                { userStore.state.count }
            </div>
            <div>
                { bookStore.state.count }
            </div>
            <div>
                <button onClick={() => {
                    userStore.actions.incId(1)
                }}>Inc Id</button>
            </div>
            <div>
                Comp1: <Comp1/>
            </div>
            
        </div>
    )
}