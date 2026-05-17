package service;

import com.fitness.fitnesscentersystem.model.User;
import com.fitness.fitnesscentersystem.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {

    private static final String FILE_PATH = "src/main/resources/data/users.txt.txt";

    // CREATE — Register a new user
    public boolean registerUser(User user) {
        if (findByUsername(user.getUsername()) != null) {
            return false; // Username already exists
        }
        user.setUserId(UUID.randomUUID().toString().substring(0, 8));
        user.setRole("USER");
        List<String> lines = FileUtil.readLines(FILE_PATH);
        lines.add(user.toFileString());
        FileUtil.writeLines(FILE_PATH, lines);
        return true;
    }

    // READ — Get all users.txt
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (String line : FileUtil.readLines(FILE_PATH)) {
            if (!line.trim().isEmpty()) {
                users.add(User.fromFileString(line));
            }
        }
        return users;
    }

    // READ — Find by username
    public User findByUsername(String username) {
        for (User u : getAllUsers()) {
            if (u.getUsername().equalsIgnoreCase(username)) return u;
        }
        return null;
    }

    // READ — Find by ID
    public User findById(String userId) {
        for (User u : getAllUsers()) {
            if (u.getUserId().equals(userId)) return u;
        }
        return null;
    }

    // UPDATE — Update user details
    public boolean updateUser(User updated) {
        List<User> users = getAllUsers();
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updated.getUserId())) {
                users.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) {
            List<String> lines = new ArrayList<>();
            for (User u : users) lines.add(u.toFileString());
            FileUtil.writeLines(FILE_PATH, lines);
        }
        return found;
    }

    // DELETE — Delete a user
    public boolean deleteUser(String userId) {
        List<User> users = getAllUsers();
        boolean removed = users.removeIf(u -> u.getUserId().equals(userId));
        if (removed) {
            List<String> lines = new ArrayList<>();
            for (User u : users) lines.add(u.toFileString());
            FileUtil.writeLines(FILE_PATH, lines);
        }
        return removed;
    }

    // LOGIN validation
    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;
    }
}
