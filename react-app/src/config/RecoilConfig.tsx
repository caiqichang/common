import { atom, selector, useRecoilValue, useSetRecoilState } from 'recoil';

export default function (key: string, state: any, getters: any, actions: any) {

    const initState = JSON.parse(JSON.stringify(state))

    const keyAtom = atom({
        key: `${key}Atom`,
        default: JSON.parse(JSON.stringify(state)),
    })

    const keySelector = selector({
        key: `${key}Selector`,
        get: ({get}) => {
            return get(keyAtom)
        },
        set: ({set}, value) => {
            set(keyAtom, JSON.parse(JSON.stringify(value)))
        },
    })

    return function () {
        const setState = useSetRecoilState(keySelector)

        let actionsWrapper: any = {}
        Object.keys(actions).forEach(k => {
            actionsWrapper[k] = function () {
                let returnValue = actions[k].apply(null, [].slice.call(arguments))
                if (returnValue instanceof Promise) {
                    return new Promise((resolve, reject) => {
                        returnValue.then(function() {
                            setState(state)
                            resolve.apply(null, [].slice.call(arguments) as any)
                        })
                        .catch(function() {
                            setState(state)
                            reject.apply(null, [].slice.call(arguments) as any)
                        });
                    });
                }else {
                    setState(state)
                    return returnValue
                } 
            }
        })

        return {
            state: useRecoilValue(keyAtom), 
            getters, 
            actions: actionsWrapper,
            reset() {
                let origin = JSON.parse(JSON.stringify(initState))
                Object.keys(state).forEach(k => state[k] = origin[k])
                setState(state)
            }
        }
    }
}