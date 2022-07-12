const {createProxyMiddleware} = require("http-proxy-middleware")

module.exports = (app: any) => {
    app.use("/dgfy", createProxyMiddleware({
        target: "http://localhost:2001",
    }))
}