package web.impl.js.sound;

import client.Signlink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Uint8Array;
import web.SoundEngine;
import web.impl.js.JSConfig;
import web.impl.js.JSMethods;
import web.impl.js.sound.howl.Howl;
import web.impl.js.sound.howl.HowlConfig;
import web.impl.js.sound.midi.Timidity;

public class JSSoundEngine extends SoundEngine {

    private final Logger logger = LoggerFactory.getLogger(JSSoundEngine.class);
    private final Howl[] wavCache = new Howl[5];
    private final Uint8Array[] midiCache = new Uint8Array[5];
    private int currentWav = -1, currentMidi = -1;

    private Timidity timidity;

    @Override
    public void init() {
        logger.info("Howl:{} Timidity:{}", Howl.isSupported(), Timidity.isSupported());
        if(Timidity.isSupported()){
            timidity = Timidity.create(JSConfig.get().timidity().getBaseUrl());
            JSMethods.export("tim", timidity);
            timidity.on("playing", () ->{
                logger.info("Playback started");
            });
            timidity.on("ended", () ->{
                logger.info("Re-playing track");
                timidity.play();
            });
        }
    }

    private String getWavUrl(){
        Uint8Array wrapped = Uint8Array.create(Signlink.savebuf.length);
        wrapped.set(Signlink.savebuf);
        JSObject blob = JSMethods.blobify(wrapped, "audio/wav");
        return JSMethods.createObjectUrl(blob);
    }

    public void update() {
        try {
            updateSounds();
            updateMusic();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateSounds(){
        if(Signlink.waveplay && Howl.isSupported() && currentWav != Signlink.wavepos){
            currentWav = Signlink.wavepos;
            System.out.println("Playing:" + Signlink.savereq + " - " + Signlink.wavepos);
            Howl sound = getSound();
        }
    }

    private void updateMusic(){
        if(Signlink.midiplay && Timidity.isSupported() && currentMidi != Signlink.midipos){
            currentMidi = Signlink.midipos;
            logger.info("Playing:{}", Signlink.midi);
            Uint8Array buf;
            if(Signlink.savebuf != null){
                buf = Uint8Array.create(Signlink.savebuf.length);
                buf.set(Signlink.savebuf);
                midiCache[Signlink.midipos] = buf;
            }else{
                buf = midiCache[Signlink.midipos];
            }
            System.out.print(Signlink.midivol);
            timidity.load(buf);
            float vol = convertVol(Signlink.midivol);
            logger.info("Timidity vol:{}", vol);
            timidity.getGainNode().getGain().setValue(vol);
            timidity.play();
        }
    }

    @Override
    public void pause() {
        if(Timidity.isSupported()){
            timidity.pause();
        }
        if(Howl.isSupported() && currentWav != -1){
            wavCache[currentWav].pause();
        }
    }

    @Override
    public void play() {
        if(Timidity.isSupported()){
            timidity.play();
        }
        if(Howl.isSupported() && currentWav != -1){
            wavCache[currentWav].play();
        }
    }

    private float convertVol(int vol){
        float res = vol + 10000;
        return res / 10000.0f;
    }

    private Howl getSound(){
        Howl cached = wavCache[Signlink.wavepos];
        if(Signlink.savebuf == null){
            return cached;
        }
        if(cached != null){
            cached.stop();
            cached.unload();
        }
        String url = getWavUrl();
        HowlConfig config = HowlConfig.create(url);
        config.setHtml5(true);
        config.setAutoplay(true);
        config.setFormat("wav");
        config.setVolume(convertVol(Signlink.wavevol));
        config.setOnStop(event -> {
            JSMethods.revokeObjectURL(url);
        });
        Howl sound = Howl.create(config);
        return wavCache[Signlink.wavepos] = sound;
    }
}
