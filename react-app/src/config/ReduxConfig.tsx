import { createSlice, configureStore, createReducer, combineReducers } from "@reduxjs/toolkit";
import { useDispatch, useSelector } from "react-redux";

const reducers: any = {};

export const store = configureStore({
    reducer: {
        '': createReducer({}, () => {}),
    },
})

export default function (key: string, state: any, getters: any, actions: any) {
    
    const initState = JSON.parse(JSON.stringify(state))
    
    const keySlice = createSlice({
        name: `${key}Slice`,
        initialState: JSON.parse(JSON.stringify(state)),
        reducers: {
            setState(state, action) {
                return JSON.parse(JSON.stringify(action.payload))
            },
        },
    })

    reducers[key] = keySlice.reducer;
    store.replaceReducer(combineReducers(reducers) as any)

    return function() {
        const dispatch = useDispatch()

        let actionsWrapper: any = {}
        Object.keys(actions).forEach(k => {
            actionsWrapper[k] = function () {
                let returnValue = actions[k].apply(null, [].slice.call(arguments))
                if (returnValue instanceof Promise) {
                    return new Promise((resolve, reject) => {
                        returnValue.then(function() {
                            dispatch(keySlice.actions.setState(state))
                            resolve.apply(null, [].slice.call(arguments) as any)
                        })
                        .catch(function() {
                            dispatch(keySlice.actions.setState(state))
                            reject.apply(null, [].slice.call(arguments) as any)
                        })
                    })
                }else {
                    dispatch(keySlice.actions.setState(state))
                    return returnValue;
                }
            }
        })

        return {
            state: useSelector((state: any) => state[key]),
            getters,
            actions: actionsWrapper,
            reset() {
                let origin = JSON.parse(JSON.stringify(initState))
                Object.keys(state).forEach(k => state[k] = origin[k])
                dispatch(keySlice.actions.setState(state))
            }
        }
    }
}