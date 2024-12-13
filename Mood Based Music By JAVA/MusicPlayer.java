import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {
    private List<Song> songList;
    private int currentSongIndex = -1;

    public MusicPlayer() {
        songList = new ArrayList<>();
    }

    public void addSong(Song song) {
        songList.add(song);
    }
    public void playSong(int index) {
        if (index >= 0 && index < songList.size()) {
            if (currentSongIndex != -1) {
                songList.get(currentSongIndex).pause();
            }
            currentSongIndex = index;
            songList.get(currentSongIndex).play();
        } else {
            System.out.println("\nSong index out of range.");
        }
    }

    public void nextSong() {
        if (currentSongIndex + 1 < songList.size()) {
            playSong(currentSongIndex + 1);
        } else {
            System.out.println("\nNo next song available.");
        }
    }

    public void previousSong() {
        if (currentSongIndex - 1 >= 0) {
            playSong(currentSongIndex - 1);
        } else {
            System.out.println("\nNo previous song available.");
        }
    }
    public void displayPlaylist(String mood) {
        List<MoodSong> filteredSongs = getSongsByMood(mood);
        System.out.println("\nSongs for mood: " + mood);
        if (filteredSongs.isEmpty()) {
            System.out.println("\nNo songs found for mood: " + mood);
        } else {
            for (MoodSong song : filteredSongs) {
                System.out.println(song.getTitle() + " by " + song.getArtist());
            }
        }
    }
    
    public List<MoodSong> getSongsByMood(String mood) {
        List<MoodSong> filteredSongs = new ArrayList<>();
        for (Song song : songList) { // Iterate through the List of Song
            if (song instanceof MoodSong) { // Check if the Song is a MoodSong
                MoodSong moodSong = (MoodSong) song; // Cast to MoodSong
                if (moodSong.getMood().equalsIgnoreCase(mood)) {
                    filteredSongs.add(moodSong); // Add to filtered list
                }
            }
        }
        return filteredSongs;
    }

    public void addSong(MoodSong song) {
        songList.add(song);
        System.out.println("\nAdded: " + song.getTitle() + " by " + song.getArtist());
    }
    public void deleteSong(int index) {
        if (index >= 0 && index < songList.size()) {
            System.out.println("\nDeleted: " + songList.get(index).getTitle());
            songList.remove(index);
            // Adjust currentSongIndex if needed
            if (currentSongIndex >= songList.size()) {
                currentSongIndex = songList.size() - 1; // Move to last song if current was deleted
            }
        } else {
            System.out.println("\nInvalid index. Cannot delete song.");
        }
    }

    // Method to update a song at a specific index
    public void updateSong(int index, MoodSong newSong) {
        if (index >= 0 && index < songList.size()) {
            System.out.println("\nUpdated: " + songList.get(index).getTitle() + " to " + newSong.getTitle());
            songList.set(index, newSong);
        } else {
            System.out.println("\nInvalid index. Cannot update song.");
        }
    }
    
    public List<MoodSong> getMoodSongs() {
        List<MoodSong> moodSongs = new ArrayList<>();
        for (Song song : songList) {
            if (song instanceof MoodSong) {
                moodSongs.add((MoodSong) song); // Add only MoodSong instances
            }
        }
        return moodSongs; // Return the list of MoodSongs
    }
}