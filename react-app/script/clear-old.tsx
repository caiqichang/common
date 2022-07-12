const fs = require("fs")
const dotenv = require("dotenv")

dotenv.config()

let outputDir = `.${process.env.BUILD_PATH || "/build"}`
fs.rmSync(outputDir, {
    recursive: true,
    force: true,
});