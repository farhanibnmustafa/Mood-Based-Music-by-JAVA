public abstract class Song {
    protected String title;
    protected String artist;
    protected String mood;

    public Song(String title, String artist, String mood) {
        this.title = title;
        this.artist = artist;
        this.mood = mood;
    }
    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public abstract void play();
    public abstract void pause();
}