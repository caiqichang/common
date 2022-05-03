"use strict";

const getSound = (key, region, voice, fileName, text) => {

    const sdk = require("microsoft-cognitiveservices-speech-sdk")

    const speechConfig = sdk.SpeechConfig.fromSubscription(key, region)
    const audioConfig = sdk.AudioConfig.fromAudioFileOutput(fileName)

    speechConfig.speechSynthesisVoiceName = voice

    const synthesizer = new sdk.SpeechSynthesizer(speechConfig, audioConfig)

    return new Promise((resolve, reject) => {
        synthesizer.speakTextAsync(text, result => {
            if (result.reason === sdk.ResultReason.SynthesizingAudioCompleted) {
                synthesizer.close()
                resolve()
            }else {
                synthesizer.close()
                reject(result.errorDetails)
            }
        }, error => {
            synthesizer.close()
            reject(error)
        })
        
    })
}

module.exports = {
    getSound,
}