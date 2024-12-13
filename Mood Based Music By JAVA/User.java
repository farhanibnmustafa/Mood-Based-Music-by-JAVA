import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private boolean isPremium;
    private MusicPlayer musicPlayer; // Each user has their own music player instance
    private Map<Integer, String> securityQuestions; // Map to store security question IDs and answers
    private Map<Integer, String> securityQuestionsMap; // Map to store security question IDs and their corresponding questions

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isPremium = false; // Default to free user
        this.musicPlayer = new MusicPlayer(); // Initialize the user's music player
        this.securityQuestions = new HashMap<>(); // Initialize the security questions map
        this.securityQuestionsMap = new HashMap<>();
        // Initialize security questions (you can modify this as needed)
        securityQuestionsMap.put(1, "\nWhat's your favourite pet?");
        securityQuestionsMap.put(2, "\nWhat's your favourite pet name?");
        securityQuestionsMap.put(3, "\nWhat's your childhood best friend's name?");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer; // Return the user's music player
    }

    public void upgradeToPremium() {
        this.isPremium = true;
    }

    // Method to change the user's password
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // Method to validate the provided password
    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    // Method to set security question and answer
    public void setSecurityQuestion(int questionId, String answer) {
        if (securityQuestionsMap.containsKey(questionId)) {
            securityQuestions.put(questionId, answer);
        } else {
            System.out.println("\nInvalid security question ID.");
        }
    }

    // Method to retrieve the security question based on ID
    public String getSecurityQuestion(int questionId) {
        return securityQuestionsMap.get(questionId); // Return the security question for the given ID
    }

    // Method to get the security question ID (assuming you want to return the keys)
    public Integer getSecurityQuestionId() {
        if (!securityQuestions.isEmpty()) {
            return securityQuestions.keySet().iterator().next(); // Return the first question ID
        }
        return null; // No questions set
    }

    // Method to get the security question answer
    public String getSecurityQuestionAnswer(int questionId) {
        return securityQuestions.get(questionId); // Return the answer for the given question ID
    }

    // Method to validate security answer
    public boolean validateSecurityAnswer(int questionId, String answer) {
        return securityQuestions.get(questionId) != null && securityQuestions.get(questionId).equals(answer);
    }
    public void setPassword(String newPassword) {
        this.password = newPassword; // Update the user's password
    }

}