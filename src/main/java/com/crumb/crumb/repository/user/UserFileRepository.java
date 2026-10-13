package com.crumb.crumb.repository.user;

import com.crumb.crumb.model.user.AdminModel;
import com.crumb.crumb.model.user.CustomerModel;
import com.crumb.crumb.model.user.UserModel;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * File-based implementation of UserRepository.
 * Persists and retrieves user data using Java File I/O (BufferedReader, BufferedWriter).
 * 
 * Objectives Satisfied:
 * - File read/write operations for persistent data storage (users.txt).
 * - CRUD operations via file manipulation.
 * - Polymorphic parsing and serialization through UserModel.toFileRecord().
 */
@Repository
public class UserFileRepository implements UserRepository {

    private static final String DATA_DIR = "data";
    private static final String FILE_NAME = "users.txt";
    private final File dataFile;

    public UserFileRepository() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.dataFile = new File(dir, FILE_NAME);
        initFileWithDefaults();
    }

    /**
     * Ensure the storage file exists. If new, populate with default seed accounts.
     */
    private synchronized void initFileWithDefaults() {
        if (!dataFile.exists() || dataFile.length() == 0) {
            try {
                if (!dataFile.exists()) {
                    dataFile.createNewFile();
                }
                List<UserModel> defaultUsers = new ArrayList<>();
                defaultUsers.add(new AdminModel("A001", "Crumb Owner", "admin@crumb.lk", "admin123", "Management"));
                defaultUsers.add(new CustomerModel("C001", "Alex Perera", "alex@example.com", "alex123",
                        "077 123 4567", "24 Flower Road, Colombo", "Regular"));
                writeAllToFile(defaultUsers);
            } catch (IOException e) {
                System.err.println("Failed to initialize users.txt file: " + e.getMessage());
            }
        }
    }

    /**
     * Read all user records from users.txt.
     * Demonstrates Polymorphism during instantiation.
     */
    @Override
    public synchronized List<UserModel> findAll() {
        List<UserModel> users = new ArrayList<>();
        if (!dataFile.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(dataFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip blank lines and comments
                }
                UserModel user = parseLineToUser(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users from " + dataFile.getAbsolutePath() + ": " + e.getMessage());
        }

        return users;
    }

    /**
     * Parse a single pipe-delimited line from users.txt into a UserModel object.
     */
    private UserModel parseLineToUser(String line) {
        // Expected format:
        // ROLE|id|name|email|password|phone|address|typeOrDept
        String[] parts = line.split("\\|", -1);
        if (parts.length < 5) {
            return null;
        }

        String role = parts[0].trim();
        String id = parts[1].trim();
        String name = parts[2].trim();
        String email = parts[3].trim();
        String password = parts[4].trim();
        String phone = parts.length > 5 ? parts[5].trim() : "";
        String address = parts.length > 6 ? parts[6].trim() : "";
        String typeOrDept = parts.length > 7 ? parts[7].trim() : "";

        if ("ADMIN".equalsIgnoreCase(role)) {
            return new AdminModel(id, name, email, password, typeOrDept);
        } else {
            return new CustomerModel(id, name, email, password, phone, address, typeOrDept);
        }
    }

    /**
     * Write all users to users.txt using BufferedWriter.
     */
    private synchronized void writeAllToFile(List<UserModel> users) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dataFile, false), StandardCharsets.UTF_8))) {
            for (UserModel user : users) {
                writer.write(user.toFileRecord());
                writer.newLine();
            }
        }
    }

    @Override
    public synchronized Optional<UserModel> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return findAll().stream()
                .filter(u -> id.equalsIgnoreCase(u.getId()))
                .findFirst();
    }

    @Override
    public synchronized Optional<UserModel> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return findAll().stream()
                .filter(u -> email.trim().equalsIgnoreCase(u.getEmail()))
                .findFirst();
    }

    @Override
    public synchronized List<UserModel> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        String lowerKeyword = keyword.trim().toLowerCase();
        return findAll().stream()
                .filter(u -> (u.getName() != null && u.getName().toLowerCase().contains(lowerKeyword)) ||
                        (u.getEmail() != null && u.getEmail().toLowerCase().contains(lowerKeyword)) ||
                        (u.getId() != null && u.getId().toLowerCase().contains(lowerKeyword)))
                .toList();
    }

    @Override
    public synchronized UserModel save(UserModel user) {
        List<UserModel> users = findAll();
        if (user.getId() == null || user.getId().trim().isEmpty()) {
            user.setId(generateNextId(user.getRole()));
            users.add(user);
        } else {
            boolean updated = false;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equalsIgnoreCase(user.getId())) {
                    users.set(i, user);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                users.add(user);
            }
        }

        try {
            writeAllToFile(users);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist user to file: " + e.getMessage(), e);
        }
        return user;
    }

    @Override
    public synchronized boolean deleteById(String id) {
        if (id == null) {
            return false;
        }
        List<UserModel> users = findAll();
        boolean removed = users.removeIf(u -> id.equalsIgnoreCase(u.getId()));
        if (removed) {
            try {
                writeAllToFile(users);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update users.txt after deletion: " + e.getMessage(), e);
            }
        }
        return removed;
    }

    @Override
    public synchronized String generateNextId(String role) {
        String prefix = "ADMIN".equalsIgnoreCase(role) ? "A" : "C";
        int maxSeq = 0;
        for (UserModel user : findAll()) {
            if (user.getId() != null && user.getId().startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(user.getId().substring(1));
                    if (num > maxSeq) {
                        maxSeq = num;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("%s%03d", prefix, maxSeq + 1);
    }
}
