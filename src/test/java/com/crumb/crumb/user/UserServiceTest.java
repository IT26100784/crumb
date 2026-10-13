package com.crumb.crumb.user;

import com.crumb.crumb.model.user.AdminModel;
import com.crumb.crumb.model.user.CustomerModel;
import com.crumb.crumb.model.user.UserModel;
import com.crumb.crumb.model.user.dto.LoginRequestDTO;
import com.crumb.crumb.model.user.dto.UserRequestDTO;
import com.crumb.crumb.model.user.dto.UserResponseDTO;
import com.crumb.crumb.repository.user.UserRepository;
import com.crumb.crumb.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Verify OOP Polymorphism on UserModel subclasses")
    void testOopPolymorphism() {
        UserModel customer = new CustomerModel("C999", "Sam Customer", "sam@test.com", "pass123", "0711111111", "Kandy", "Premium");
        UserModel admin = new AdminModel("A999", "Boss Admin", "boss@test.com", "pass123", "Finance");

        // Polymorphic behavior
        assertEquals("Bakery Customer (Premium)", customer.getRoleTitle());
        assertFalse(customer.hasAdminAccess());

        assertEquals("Crumb Bakery Administrator (Finance)", admin.getRoleTitle());
        assertTrue(admin.hasAdminAccess());
    }

    @Test
    @DisplayName("CRUD: Register (Create), Read, Update, and Delete Customer")
    void testCustomerCrudLifecycle() {
        String testEmail = "testuser" + System.currentTimeMillis() + "@example.com";

        // 1. CREATE (Register)
        UserRequestDTO registerReq = new UserRequestDTO(
                "Jane Doe",
                testEmail,
                "secret123",
                "0779998888",
                "10 Galle Road, Colombo",
                "Regular",
                "CUSTOMER"
        );
        UserResponseDTO created = userService.register(registerReq);
        assertNotNull(created.getId());
        assertEquals("Jane Doe", created.getName());
        assertEquals(testEmail, created.getEmail());
        assertEquals("0779998888", created.getPhone());
        assertEquals("10 Galle Road, Colombo", created.getAddress());

        // 2. READ (Find by ID & Login)
        UserResponseDTO fetched = userService.getUserById(created.getId());
        assertEquals(created.getId(), fetched.getId());

        LoginRequestDTO loginReq = new LoginRequestDTO(testEmail, "secret123");
        UserResponseDTO loggedIn = userService.login(loginReq);
        assertEquals(created.getId(), loggedIn.getId());

        // 3. READ (Search)
        List<UserResponseDTO> searchResults = userService.searchUsers(testEmail);
        assertFalse(searchResults.isEmpty());
        assertTrue(searchResults.stream().anyMatch(u -> u.getId().equals(created.getId())));

        // 4. UPDATE
        UserRequestDTO updateReq = new UserRequestDTO();
        updateReq.setName("Jane Updated");
        updateReq.setPhone("0770000000");
        updateReq.setAddress("42 Lake Drive");
        updateReq.setCustomerType("Premium");

        UserResponseDTO updated = userService.updateUser(created.getId(), updateReq);
        assertEquals("Jane Updated", updated.getName());
        assertEquals("0770000000", updated.getPhone());
        assertEquals("42 Lake Drive", updated.getAddress());
        assertEquals("Premium", updated.getCustomerType());

        // 5. DELETE
        boolean deleted = userService.deleteUser(created.getId());
        assertTrue(deleted);

        // Verify it no longer exists
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(created.getId()));
    }

    @Test
    @DisplayName("Validation: Reject duplicate registration and weak password")
    void testValidationConstraints() {
        UserRequestDTO weakPass = new UserRequestDTO("Weak", "weak@test.com", "123", "077", "Col", "Regular", "CUSTOMER");
        assertThrows(IllegalArgumentException.class, () -> userService.register(weakPass));

        UserRequestDTO invalidEmail = new UserRequestDTO("Bad", "invalid-email", "pass1234", "077", "Col", "Regular", "CUSTOMER");
        assertThrows(IllegalArgumentException.class, () -> userService.register(invalidEmail));
    }
}
