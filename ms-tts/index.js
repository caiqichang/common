"use strict"

const input = require("./data.js")
const tts = require("./tts.js")
const fs = require("fs")

const region = "eastus"
const voice = "zh-CN-XiaoxiaoNeural"
const fileName = "sound"
const outDir = "./out"

fs.rmSync(outDir, {
    recursive: true,
    force: true,
})
fs.mkdirSync(outDir)

async function getSoundAsync() {
    for (let i = 0; i < input.text.length; i++) {
        let name = `${outDir}/${fileName}-${i + 1}.wav`
        console.log(`${name}: getting sound ...`)
        await tts.getSound(input.key, region, voice, name, input.text[i])
            .then(() => console.log(`${name}: finished.`))
            .catch(err => console.log(err))
    }
}

getSoundAsync()

