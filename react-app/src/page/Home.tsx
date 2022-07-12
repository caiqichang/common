export default () => {

    const doClick = () => {
        let i = 0
        i += 5
        console.log(i)
    }

    return (
        <div>
            <div>Home</div>
            <button onClick={doClick}>Test</button>
        </div>
    )
}