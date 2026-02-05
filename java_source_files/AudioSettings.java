public class AudioSettings {
    private static AudioSettings instance;
    
    private double bgmVolume;
    private double sfxVolume;

    private AudioSettings() {
        bgmVolume = 0.3;
        sfxVolume = 0.5;
    }

    public static AudioSettings getInstance() {
        if (instance == null) {
            instance = new AudioSettings();
        }
        return instance;
    }

    public double getBgmVolume() { return bgmVolume; }
    public double getSfxVolume() { return sfxVolume; }

    public void setBgmVolume(double volume) { 
        this.bgmVolume = Math.max(0.0, Math.min(1.0, volume)); 
    }
    
    public void setSfxVolume(double volume) { 
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume)); 
    }
}
