public class MoodSong extends Song {
    public MoodSong(String title, String artist, String mood) {
        super(title, artist, mood); // Call the superclass constructor
    }

    @Override
    public void play() {
        System.out.println("Playing: " + getTitle() + " by " + getArtist());
    }

    @Override
    public void pause() {
        System.out.println("Paused: " + getTitle());
    }

    public String getMood() {
        return mood; // Access mood directly since it's protected
    }
}