package com.shopme.admin.service;

import com.shopme.admin.dto.request.UserCreateRequest;
import com.shopme.admin.dto.request.UserUpdateRequest;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.RoleResponse;
import com.shopme.admin.dto.response.UserDetailResponse;
import com.shopme.admin.dto.response.UserListResponse;
import com.shopme.admin.mapper.RoleMapper;
import com.shopme.admin.mapper.UserMapper;
import com.shopme.admin.repository.RoleRepository;
import com.shopme.admin.repository.UserRepository;
import com.shopme.admin.service.impl.UserServiceImpl;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private FileUploadService fileUploadService;

    private UserCreateRequest userCreateRequest;
    private User user;
    private Role role;
    private RoleResponse roleResponse;
    private UserDetailResponse userDetailResponse;
    private UserListResponse userListResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        role = new Role();
        role.setId(1);
        role.setName("Admin");
        role.setDescription("Administrator");

        Set<Integer> roleIds = new HashSet<>();
        roleIds.add(1);

        roleResponse = new RoleResponse();
        roleResponse.setId(1);
        roleResponse.setName("Admin");
        roleResponse.setDescription("Administrator");

        userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail("test@example.com");
        userCreateRequest.setPassword("password");
        userCreateRequest.setFirstName("John");
        userCreateRequest.setLastName("Doe");
        userCreateRequest.setEnabled(true);
        userCreateRequest.setRoleIds(roleIds);

        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEnabled(true);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userDetailResponse = new UserDetailResponse();
        userDetailResponse.setId(1);
        userDetailResponse.setEmail("test@example.com");
        userDetailResponse.setFirstName("John");
        userDetailResponse.setLastName("Doe");
        userDetailResponse.setEnabled(true);
        userDetailResponse.setRoleIds(roleIds);

        userListResponse = new UserListResponse();
        userListResponse.setId(1);
        userListResponse.setEmail("test@example.com");
        userListResponse.setFirstName("John");
        userListResponse.setLastName("Doe");
        userListResponse.setEnabled(true);
        userListResponse.setRoles(roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
    }

    @Nested
    @DisplayName("Create User Operations")
    class CreateUserTests {
        @Test
        @DisplayName("Should create user successfully")
        void createUser_Success() {
            // Arrange
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userMapper.toEntity(any(UserCreateRequest.class))).thenReturn(user);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(roleRepository.findById(1)).thenReturn(Optional.of(role));
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toUserDetailResponse(any(User.class))).thenReturn(userDetailResponse);

            // Act
            UserDetailResponse result = userService.createUser(userCreateRequest);

            // Assert
            assertNotNull(result);
            assertEquals("test@example.com", result.getEmail());
            verify(userRepository).save(user);
            verify(userMapper).toUserDetailResponse(user);
        }

        @Test
        @DisplayName("Should create user with image successfully")
        void createUser_WithImage_Success() {
            // Arrange
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    "test image content".getBytes());

            userCreateRequest.setImage(file);

            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userMapper.toEntity(any(UserCreateRequest.class))).thenReturn(user);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(roleRepository.findById(1)).thenReturn(Optional.of(role));
            when(fileUploadService.userPhotosUpload(any(MultipartFile.class), anyInt()))
                    .thenReturn("test.jpg");
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toUserDetailResponse(any(User.class))).thenReturn(userDetailResponse);

            // Act
            UserDetailResponse result = userService.createUser(userCreateRequest);

            // Assert
            assertNotNull(result);
            verify(fileUploadService).userPhotosUpload(any(MultipartFile.class), anyInt());
            verify(userMapper).toUserDetailResponse(user);
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();
            assertEquals("test.jpg", savedUser.getPhotos());
        }

        @Test
        @DisplayName("Should throw exception when email exists")
        void createUser_EmailExists_ThrowsException() {
            // Arrange
            when(userRepository.existsByEmail(anyString())).thenReturn(true);

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                userService.createUser(userCreateRequest);
            });

            assertEquals("Email already exists", exception.getMessage());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Update User Operations")
    class UpdateUserTests {
        @Test
        @DisplayName("Should update user successfully")
        void updateUser_Success() {
            // Arrange
            UserUpdateRequest updateRequest = new UserUpdateRequest();
            updateRequest.setEmail("updated@example.com");
            updateRequest.setFirstName("Updated");
            updateRequest.setLastName("User");
            updateRequest.setEnabled(true);
            updateRequest.setRoleIds(Set.of(1));

            User existingUser = new User();
            existingUser.setId(1);
            existingUser.setEmail("test@example.com");

            User updatedUser = new User();
            updatedUser.setId(1);
            updatedUser.setEmail("updated@example.com");
            updatedUser.setFirstName("Updated");
            updatedUser.setLastName("User");
            updatedUser.setEnabled(true);
            updatedUser.setRoles(Set.of(role));

            when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
            when(roleRepository.findById(1)).thenReturn(Optional.of(role));
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            when(userMapper.toUserDetailResponse(any(User.class))).thenReturn(userDetailResponse);

            // Act
            UserDetailResponse result = userService.updateUser(1, updateRequest);

            // Assert
            assertNotNull(result);
            verify(userRepository).save(any(User.class));
            verify(userMapper).toUserDetailResponse(any(User.class));
        }

        @Test
        @DisplayName("Should update user with image successfully")
        void updateUser_WithImage_Success() {
            // Arrange
            UserUpdateRequest updateRequest = new UserUpdateRequest();
            updateRequest.setEmail("updated@example.com");
            updateRequest.setFirstName("Updated");
            updateRequest.setLastName("User");
            updateRequest.setEnabled(true);
            updateRequest.setRoleIds(Set.of(1));

            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    "test image content".getBytes());
            updateRequest.setImage(file);

            User existingUser = new User();
            existingUser.setId(1);
            existingUser.setEmail("test@example.com");

            User updatedUser = new User();
            updatedUser.setId(1);
            updatedUser.setEmail("updated@example.com");
            updatedUser.setFirstName("Updated");
            updatedUser.setLastName("User");
            updatedUser.setEnabled(true);
            updatedUser.setRoles(Set.of(role));

            when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
            when(roleRepository.findById(1)).thenReturn(Optional.of(role));
            when(fileUploadService.userPhotosUpload(file, 1)).thenReturn("test.jpg");
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            when(userMapper.toUserDetailResponse(any(User.class))).thenReturn(userDetailResponse);

            // Act
            UserDetailResponse result = userService.updateUser(1, updateRequest);

            // Assert
            assertNotNull(result);
            verify(fileUploadService).userPhotosUpload(file, 1);
            verify(userRepository).save(any(User.class));
            verify(userMapper).toUserDetailResponse(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void updateUser_NotFound_ThrowsException() {
            // Arrange
            when(userRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                userService.updateUser(999, any(UserUpdateRequest.class));
            });

            assertEquals("User not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Delete User Operations")
    class DeleteUserTests {
        @Test
        @DisplayName("Should delete user successfully")
        void deleteUser_Success() {
            // Arrange
            when(userRepository.findById(1)).thenReturn(Optional.of(user));
            doNothing().when(userRepository).delete(user);

            // Act
            userService.deleteUser(1);

            // Assert
            verify(userRepository).delete(user);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void deleteUser_NotFound_ThrowsException() {
            // Arrange
            when(userRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                userService.deleteUser(999);
            });

            assertEquals("User not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Query User Operations")
    class QueryUserTests {
        @Test
        @DisplayName("Should get all users successfully")
        void getAllUsers_Success() {
            // Arrange
            List<User> users = Arrays.asList(user);
            Page<User> userPage = new PageImpl<>(users);
            Map<String, String> params = new HashMap<>();

            params.put("page", "0");
            params.put("limit", "10");
            params.put("sortField", "id");
            params.put("sortDir", "asc");

            when(userRepository.findAll(anyString(), any(Pageable.class))).thenReturn(userPage);
            when(userMapper.toUserListResponse(any(User.class))).thenReturn(userListResponse);

            // Act
            ListResponse<UserListResponse> result = userService.listByPage(params);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalPages());
            assertEquals("test@example.com", result.getContent().get(0).getEmail());
            verify(userRepository).findAll(anyString(), any(Pageable.class));
            verify(userMapper).toUserListResponse(user);
        }

        @Test
        @DisplayName("Should get user by id successfully")
        void getUserById_Success() {
            // Arrange
            when(userRepository.findById(1)).thenReturn(Optional.of(user));
            when(userMapper.toUserDetailResponse(user)).thenReturn(userDetailResponse);

            // Act
            UserDetailResponse result = userService.getUserById(1);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("test@example.com", result.getEmail());
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void getUserById_NotFound_ThrowsException() {
            // Arrange
            when(userRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                userService.getUserById(999);
            });

            assertEquals("User not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Role Operations")
    class RoleTests {
        @Test
        @DisplayName("Should get all roles successfully")
        void getAllRoles_Success() {
            // Arrange
            List<Role> roles = Arrays.asList(role);
            when(roleRepository.findAll()).thenReturn(roles);
            when(roleMapper.toRoleResponse(any(Role.class))).thenReturn(roleResponse);

            // Act
            List<RoleResponse> result = userService.getAllRoles();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Admin", result.get(0).getName());
            verify(roleRepository).findAll();
            verify(roleMapper).toRoleResponse(any(Role.class));
        }
    }
}