package com.crumb.crumb.service.user;

import com.crumb.crumb.model.user.AdminModel;
import com.crumb.crumb.model.user.CustomerModel;
import com.crumb.crumb.model.user.UserModel;
import com.crumb.crumb.model.user.dto.LoginRequestDTO;
import com.crumb.crumb.model.user.dto.UserRequestDTO;
import com.crumb.crumb.model.user.dto.UserResponseDTO;
import com.crumb.crumb.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Service class handling business logic, validations, and MySQL database transactions.
 * 
 * Objectives Satisfied:
 * - Implements Create, Read, Update, Delete (CRUD) operations for MySQL database.
 * - Enforces business rules and validation (Email format, password constraints, duplicate checks).
 * - Seeds default admin and customer data if MySQL users table is empty.
 */
@Service
public class UserService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Pre-populate MySQL database with default test accounts if empty.
     */
    @PostConstruct
    @Transactional
    public void seedInitialUsers() {
        try {
            if (userRepository.count() == 0) {
                userRepository.save(new AdminModel("A001", "Crumb Owner", "admin@crumb.lk", "admin123", "Management"));
                userRepository.save(new CustomerModel("C001", "Alex Perera", "alex@example.com", "alex123",
                        "077 123 4567", "24 Flower Road, Colombo", "Regular"));
            }
        } catch (Exception e) {
            // Log warning if database is still starting up
            System.err.println("Database seeding note: " + e.getMessage());
        }
    }

    /**
     * CREATE: Register a new customer or administrator in MySQL.
     */
    @Transactional
    public UserResponseDTO register(UserRequestDTO request) {
        validateUserRequest(request, true);

        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException("A user with this email address already exists.");
        }

        String assignedId = generateNextId(request.getRole());
        UserModel user;
        if ("ADMIN".equalsIgnoreCase(request.getRole())) {
            user = new AdminModel(assignedId, request.getName(), normalizedEmail, request.getPassword(), "Management");
        } else {
            user = new CustomerModel(assignedId, request.getName(), normalizedEmail, request.getPassword(),
                    request.getPhone(), request.getAddress(),
                    request.getCustomerType() != null ? request.getCustomerType() : "Regular");
        }

        UserModel saved = userRepository.save(user);
        return UserResponseDTO.fromEntity(saved);
    }

    /**
     * READ: Authenticate user login credentials from MySQL.
     */
    public UserResponseDTO login(LoginRequestDTO request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email address is required.");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        String normalizedEmail = request.getEmail().trim().toLowerCase();
        UserModel user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return UserResponseDTO.fromEntity(user);
    }

    /**
     * READ: Get a user by ID from MySQL.
     */
    public UserResponseDTO getUserById(String id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID '" + id + "' was not found."));
        return UserResponseDTO.fromEntity(user);
    }

    /**
     * READ: Get all registered users from MySQL.
     */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }

    /**
     * READ: Search users by keyword (matches name, email, or ID).
     */
    public List<UserResponseDTO> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUsers();
        }
        return userRepository.search(keyword.trim()).stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }

    /**
     * UPDATE: Update existing user information in MySQL.
     */
    @Transactional
    public UserResponseDTO updateUser(String id, UserRequestDTO request) {
        UserModel existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID '" + id + "' was not found."));

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            existingUser.setName(request.getName().trim());
        }

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String newEmail = request.getEmail().trim().toLowerCase();
            if (!EMAIL_PATTERN.matcher(newEmail).matches()) {
                throw new IllegalArgumentException("Invalid email format.");
            }
            Optional<UserModel> userWithSameEmail = userRepository.findByEmail(newEmail);
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equalsIgnoreCase(id)) {
                throw new IllegalArgumentException("Email address is already in use by another account.");
            }
            existingUser.setEmail(newEmail);
        }

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            if (request.getPassword().length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters.");
            }
            existingUser.setPassword(request.getPassword());
        }

        if (existingUser instanceof CustomerModel customer) {
            if (request.getPhone() != null) {
                customer.setPhone(request.getPhone());
            }
            if (request.getAddress() != null) {
                customer.setAddress(request.getAddress());
            }
            if (request.getCustomerType() != null && !request.getCustomerType().trim().isEmpty()) {
                customer.setCustomerType(request.getCustomerType());
            }
        } else if (existingUser instanceof AdminModel admin) {
            if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
                admin.setDepartment(request.getAddress());
            }
        }

        UserModel updated = userRepository.save(existingUser);
        return UserResponseDTO.fromEntity(updated);
    }

    /**
     * DELETE: Remove user account from MySQL by ID.
     */
    @Transactional
    public boolean deleteUser(String id) {
        if (id != null && userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Generate sequential IDs (C001, C002, A001, etc.)
     */
    private synchronized String generateNextId(String role) {
        String prefix = "ADMIN".equalsIgnoreCase(role) ? "A" : "C";
        long count = userRepository.count() + 1;
        String candidate = String.format("%s%03d", prefix, count);
        while (userRepository.existsById(candidate)) {
            count++;
            candidate = String.format("%s%03d", prefix, count);
        }
        return candidate;
    }

    /**
     * Validation helper for incoming User requests.
     */
    private void validateUserRequest(UserRequestDTO request, boolean isNewRegistration) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (request.getEmail() == null || !EMAIL_PATTERN.matcher(request.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("A valid email address is required.");
        }
        if (isNewRegistration) {
            if (request.getPassword() == null || request.getPassword().length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters long.");
            }
        }
    }
}
