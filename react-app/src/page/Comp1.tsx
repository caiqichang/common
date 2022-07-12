import UserStore from "../store/UserStore"

export default () => {

    const userStore = UserStore()

    return (

        <div>
            <div>{userStore.getters.formatId()}</div>
            <div>
                <button onClick={userStore.reset}>reset</button>
            </div>
        </div>
    )
}