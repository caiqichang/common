import StoreConfig from "../config/StoreConfig";

const key = "BookStore"

const initState = () => {
    return {
        id: "123456",
        count: 0,
    }
}

const state = initState();

const getters = {
    formatId() {
        return `id is ${state.id} ${state.count}`
    }
}

const actions = {
    incId(c: number) {
        state.count += c
    }
}

export default StoreConfig(key, state, getters, actions);