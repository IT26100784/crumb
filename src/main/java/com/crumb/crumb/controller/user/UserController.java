package com.crumb.crumb.controller.user;

import com.crumb.crumb.model.user.dto.ApiResponse;
import com.crumb.crumb.model.user.dto.LoginRequestDTO;
import com.crumb.crumb.model.user.dto.UserRequestDTO;
import com.crumb.crumb.model.user.dto.UserResponseDTO;
import com.crumb.crumb.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller exposing User Management endpoints.
 * 
 * Objectives Satisfied:
 * - Minimum 3 CRUD operations:
 *   1. CREATE: POST /api/users/register
 *   2. READ:   GET /api/users, GET /api/users/{id}, GET /api/users/search
 *   3. UPDATE: PUT /api/users/{id}
 *   4. DELETE: DELETE /api/users/{id}
 * - Authentication: POST /api/users/login
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * CREATE: Register a new user.
     * Endpoint: POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO created = userService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User registered successfully.", created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * AUTH: Log in user.
     * Endpoint: POST /api/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        try {
            UserResponseDTO user = userService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful.", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * READ: Get all users.
     * Endpoint: GET /api/users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        try {
            List<UserResponseDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully.", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve users: " + e.getMessage()));
        }
    }

    /**
     * READ: Get single user by ID.
     * Endpoint: GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable String id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success("User found.", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch user: " + e.getMessage()));
        }
    }

    /**
     * READ: Search users by keyword (matches name, email, or ID).
     * Endpoint: GET /api/users/search?keyword=alex
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> searchUsers(@RequestParam(required = false, defaultValue = "") String keyword) {
        try {
            List<UserResponseDTO> results = userService.searchUsers(keyword);
            return ResponseEntity.ok(ApiResponse.success("Search completed.", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to search users: " + e.getMessage()));
        }
    }

    /**
     * UPDATE: Update user profile details.
     * Endpoint: PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@PathVariable String id, @RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO updated = userService.updateUser(id, request);
            return ResponseEntity.ok(ApiResponse.success("User profile updated successfully.", updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update user: " + e.getMessage()));
        }
    }

    /**
     * DELETE: Delete user account by ID.
     * Endpoint: DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("User deleted successfully.", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID '" + id + "' not found."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete user: " + e.getMessage()));
        }
    }
}
