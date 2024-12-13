import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MusicApp {
    public static void clearConsole() {
        try {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("\nError clearing console: " + e.getMessage());
        }
    }
    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted: " + e.getMessage());
        }
    }
    private static List<User> users = new ArrayList<>(); // List to store users
    private static User currentUser   = null; // Current logged-in user
    private static final String USER_DATA_FILE = "users.txt"; // File to store user data
    private static final String PLAYLIST_DATA_FILE = "playlists.txt"; // File to store playlists
    public static void main(String[] args) {
        loadUsersFromFile();
         // Load users from file at the start
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;

        while (!validChoice) {
            clearConsole();
            System.out.println("----------------------------");
            System.out.println("\n| Welcome to the Music App! |");
            System.out.println("----------------------------");
            System.out.println("\nAre you a new user? \n\n  1. Registration \n\nIf not then,\n\n  2. Login");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
        
            switch (choice) {
                case 1 -> {
                    // Registration
                    clearConsole();
                    registerUser (scanner);
                    validChoice = true; // Exit the loop after registration
                    // Exit the switch
                }
                case 2 -> {
                    // Login
                    clearConsole();
                    currentUser  = loginUser (scanner);
                    if (currentUser  == null) {
                        System.out.println("Invalid username or password. Exiting...");
                        return; // Exit if login fails
                    }
                    validChoice = true; // Exit the loop after successful login
                    // Exit the switch
                }
                default -> System.out.println("Invalid choice. Please select 1 for Registration or 2 for Login.");
                // The loop will continue, prompting the user again
                // Exit the switch for invalid choice
            }
        }

        // Main loop for the music app
        while (true) {
            displayMenu();
            int choice1 = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            switch (choice1) {
                case 1 -> playSong(scanner);
                case 2 -> currentUser  .getMusicPlayer().nextSong();
                case 3 -> currentUser  .getMusicPlayer().previousSong();
                case 4 -> displayPlaylist(scanner);
                case 5 -> addSong(scanner);
                case 6 -> deleteSong(scanner);
                case 7 -> updateSong(scanner);
                case 8 -> displayuserinfo();
                case 9 -> {
                    System.out.println("Logging Out...");
                    sleep(2000);
                    clearConsole();
                    System.out.println("----------------------------");
                    System.out.println("\n| Welcome to the Music App! |");
                    System.out.println("----------------------------");
                    System.out.println("\nAre you a new user? \n\n  1. Registration \n\nIf not then,\n\n  2. Login");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    switch (choice) {
                    case 1 -> {
                        // Registration
                        clearConsole();
                        registerUser (scanner);
                        validChoice = true; // Exit the loop after registration
                        // Exit the switch
                    }
                    case 2 -> {
                        // Login
                        clearConsole();
                        currentUser  = loginUser (scanner);
                        if (currentUser  == null) {
                            System.out.println("Invalid username or password. Exiting...");
                            return; // Exit if login fails
                        }
                        validChoice = true; // Exit the loop after successful login
                        // Exit the switch
                    }
                    default -> System.out.println("Invalid choice. Please select 1 for Registration or 2 for Login.");
                    // The loop will continue, prompting the user again
                    // Exit the switch for invalid choice
                    }
                                    return;
                                }
                                default -> System.out.println("Invalid choice. Please try again.");
                            }
                        }
                    }
    private static void displayWelcomeMessage(User user) {
    
        System.out.println("\nWelcome, " + user.getUsername() + "! We're glad to have you back...\n");
    }
    private static User findUserByUsername(String username) {
        for (User  user : users) {
            if (user.getUsername().equals(username)) {
                return user; // Return the user if found
            }
        }
        return null; // Return null if no user is found with the given username
    }

    private static void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Assuming username, password, security question, answer are comma-separated
                if (parts.length == 4) {
                    User user = new User(parts[0], parts[1]);
                    user.setSecurityQuestion(Integer.parseInt(parts[2]), parts[3]); // Assuming parts[2] is the question id
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.out.println("\nError loading user data: " + e.getMessage());
        }
    }
    private static void displayuserinfo() {
        clearConsole();
        if (currentUser  == null) {
            System.out.println("\nNo user is currently logged in.");
            return;
        }
    
        System.out.println("\n\nUser  Information:\n");
        System.out.println("Username: " + currentUser .getUsername());
        
        // Assuming you have a method in User class to get playlists
        List<MoodSong> songs = currentUser .getMusicPlayer().getMoodSongs(); // Retrieve songs from the user's music player
        System.out.println("\nNumber of songs in your playlist: " + songs.size());
        sleep(1500);
        // Display the user's playlists if applicable
        System.out.println("\nYour Playlists:\n");
        if (songs.isEmpty()) {
            System.out.println("\nNo songs found in your playlist.");
        } else {
            for (int i = 0; i < songs.size(); i++) {
                MoodSong song = songs.get(i);
                System.out.println((i + 1) + ". " + song.getTitle() + " by " + song.getArtist() + " (Mood: " + song.getMood() + ")");
            }
        }
    }
    private static void loadPlaylistsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PLAYLIST_DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Assuming username and songs are comma-separated
                if (parts.length > 1) {
                    User user = findUserByUsername(parts[0]);
                    if (user != null) {
                        for (int i = 1; i < parts.length; i++) {
                            String[] songDetails = parts[i].split(":");
                            if (songDetails.length == 3) { // Assuming songName:artist:mood format
                                MoodSong song = new MoodSong(songDetails[0], songDetails[1], songDetails[2]);
                                user.getMusicPlayer().addSong(song);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading playlists: " + e.getMessage());
        }
    }
    private static void savePlaylistsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PLAYLIST_DATA_FILE))) {
            for (User  user : users) {
                bw.write(user.getUsername());
                List<MoodSong> songs = user.getMusicPlayer().getMoodSongs(); // Assuming you have a method to get all songs
                for (MoodSong song : songs) {
                    bw.write("," + song.getTitle() + ":" + song.getArtist() + ":" + song.getMood());
                }
                bw.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving playlists: " + e.getMessage());
        }
    }
    
    private static void registerUser (Scanner scanner) {
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();
        System.out.print("\nEnter password: ");
        String password = scanner.nextLine();

        System.out.println("\n\nSelect security Question: \n1. What's your favourite pet?\n2. What's your favourite pet name?\n3. What's your childhood best friend's name?");
        int secque = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.println("\nYour Answer: ");
        String answer = scanner.nextLine();

        User user = new User(username, password);
        user.setSecurityQuestion(secque, answer); // Assuming you have a method to set this
        users.add(user); // Add user to the list
        saveUserToFile(user); // Save user to the file
        clearConsole();
        System.out.println("\n\nRegistration Successful!");
        sleep(2000);
        clearConsole();
        System.out.println("\nWelcome, " + user.getUsername() + "! We're glad to have you back...\n");
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private static void saveUserToFile(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_DATA_FILE, true))) {
            Integer securityQuestionId = user.getSecurityQuestionId();
            String securityQuestionAnswer = user.getSecurityQuestionAnswer(securityQuestionId);
            bw.write(user.getUsername() + "," + user.getPassword() + "," + securityQuestionId + "," + securityQuestionAnswer + "\n");
        } catch (IOException e) {
            System.out.println("\nError saving user data: " + e.getMessage());
            e.printStackTrace(); // Optional: Print stack trace for debugging
        }
    }
    private static User loginUser (Scanner scanner) {
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();
        User user = findUserByUsername(username);
        
        if (user == null) {
            System.out.println("\nUser  not found.");
            return null;
        }
    
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("\nEnter password: ");
            String password = scanner.nextLine();
    
            if (user.validatePassword(password)) {
                displayWelcomeMessage(user);
                return user; // Return the user if credentials match
            } else {
                attempts++;
                System.out.println("\nInvalid password. Attempts left: " + (3 - attempts));
            }
        }
    
        // If the user fails to log in after 3 attempts
        System.out.println("\nToo many failed attempts. Would you like to reset your password? (yes/no)");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            resetPassword(scanner, user);
        } else {
            System.out.println("\nExiting...");
        }
        
        return null; // Return null if login fails
    }

    
    private static void resetPassword(Scanner scanner, User user) {
        // Get the security question ID
        int securityQuestionId = user.getSecurityQuestionId(); // Assuming you have this method
        // Ask the user for the security question
        System.out.println("\nSecurity Question: " + user.getSecurityQuestion(securityQuestionId)); // Pass the security question ID
        System.out.print("\nYour answer: ");
        String answer = scanner.nextLine();
    
        if (user.validateSecurityAnswer(securityQuestionId, answer)) {
            System.out.print("\nEnter your new password: ");
            String newPassword = scanner.nextLine();
            user.setPassword(newPassword); // Assuming you have a method to set the password
            System.out.println("\nPassword reset successfully!");
            saveUserToFile(user); // Save the updated user data
        } else {
            System.out.println("\nIncorrect answer to the security question.");
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n1. Play Song\n2. Next Song\n3. Previous Song\n4. Display Playlist\n5. Add song\n6. Delete song\n7. Update Playlist\n8. Display User Info\n9. Exit");
    }

    private static void playSong(Scanner scanner) {
        System.out.print("\nEnter song no. to play: ");
        int index = scanner.nextInt();
        clearConsole();
        currentUser .getMusicPlayer().playSong(index);
    }
    private static void displayPlaylist(Scanner scanner) {
        // Load playlists from file to ensure the latest data is available
        clearConsole();
        loadPlaylistsFromFile();
        
        // Retrieve all songs from the current user's music player
        List<MoodSong> songs = currentUser .getMusicPlayer().getMoodSongs(); // Assuming this method retrieves all songs
    
        if (songs.isEmpty()) {
            System.out.println("\nNo songs found in your playlist.");
        } else {
        clearConsole();
            System.out.println("\nYour Playlist:");
            for (int i = 0; i < songs.size(); i++) {
                MoodSong song = songs.get(i);
                System.out.println((i + 1) + ". " + song.getTitle() + " by " + song.getArtist() + " (Mood: " + song.getMood() + ")");
            }
        }
    }

    private static void addSong(Scanner scanner) {
        System.out.print("\nEnter song name: ");
        String songName = scanner.nextLine();
        System.out.print("\nEnter artist name: ");
        String artistName = scanner.nextLine();
        System.out.print("\nEnter mood: ");
        String mood = scanner.nextLine();
        currentUser .getMusicPlayer().addSong(new MoodSong(songName, artistName, mood));
        System.out.println("\nSong added successfully!");
        savePlaylistsToFile(); 
    }

    private static void deleteSong(Scanner scanner) {
        System.out.print("\nEnter song no. to delete: ");
        int deleteIndex = scanner.nextInt();
        currentUser .getMusicPlayer().deleteSong(deleteIndex);
        System.out.println("\nSong deleted successfully!");
        savePlaylistsToFile(); 
    }

    private static void updateSong(Scanner scanner) {
        System.out.print("\nEnter song no. to update: ");
        int updateIndex = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.print("\nEnter new song name: ");
        String newSongName = scanner.nextLine();
        System.out.print("\nEnter new artist name: ");
        String newArtistName = scanner.nextLine();
        System.out.print("\nEnter new mood: ");
        String newMood = scanner.nextLine();
        currentUser .getMusicPlayer().updateSong(updateIndex, new MoodSong(newSongName, newArtistName, newMood));
        System.out.println("\nSong updated successfully!");
        savePlaylistsToFile(); 
    }
    
    
}