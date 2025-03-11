package com.shopme.admin.service.impl;

import com.shopme.admin.dto.request.UpdateProfileRequest;
import com.shopme.admin.dto.request.UserCreateRequest;
import com.shopme.admin.dto.request.UserUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.mapper.RoleMapper;
import com.shopme.admin.mapper.UserMapper;
import com.shopme.admin.repository.RoleRepository;
import com.shopme.admin.repository.UserRepository;
import com.shopme.admin.service.FileUploadService;
import com.shopme.admin.service.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    private static final String DEFAULT_SORT_FIELD = "firstName";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_USERS_PER_PAGE = 4;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private FileUploadService fileUploadService;
    private UserMapper userMapper;
    private RoleMapper roleMapper;

    private String getUserPhotoURL(User user) {
        return fileUploadService.getUserPhotosURL(user.getId(), user.getPhotos());
    }

    @Override
    public ListResponse<UserListResponse> listByPage(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_USERS_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);
        String keyword = params.getOrDefault("keyword", "");

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAll(keyword, pageable);
        List<UserListResponse> userListResponses = userPage.getContent().stream()
                .map(user -> {
                    var userDto = userMapper.toUserListResponse(user);
                    userDto.setPhotos(getUserPhotoURL(user));
                    return userDto;
                })
                .collect(Collectors.toList());

        return ListResponse.<UserListResponse>builder()
                .content(userListResponses)
                .totalPages(userPage.getTotalPages())
                .build();
    }

    @Transactional
    public UserDetailResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set roles
        Set<Role> roles = request.getRoleIds().stream()
                .map(roleRepository::findById)
//                exception role not found
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userRepository.save(user);

        // Handle image upload if present
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(request.getImage().getOriginalFilename()));
            fileUploadService.userPhotosUpload(request.getImage(), user.getId());
            user.setPhotos(fileName);
        }

        return userMapper.toUserDetailResponse(user);
    }

    public List<RoleResponse> getAllRoles() {
        List<Role> roles = Streamable.of(roleRepository.findAll()).toList();
        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public UserDetailResponse getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        var userDto = userMapper.toUserDetailResponse(user);
        userDto.setPhotos(getUserPhotoURL(user));
        return userDto;
    }

    @Override
    public UserDetailResponse updateUser(Integer id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setEnabled(request.getEnabled());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Set roles
        Set<Role> roles = request.getRoleIds().stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        user.setRoles(roles);

        // Handle image upload if present
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(request.getImage().getOriginalFilename()));
            fileUploadService.userPhotosUpload(request.getImage(), user.getId());
            user.setPhotos(fileName);
        }

        userRepository.save(user);
        return userMapper.toUserDetailResponse(user);
    }

    @Override
    public void updateUserStatus(Integer id, boolean status) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(status);
        userRepository.save(user);
    }

    @Override
    public List<UserExportResponse> listAllForExport() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserExportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long userId = jwt.getClaim("userId");
            return userId.intValue();
        } catch (RuntimeException e) {
            throw new RuntimeException("Unauthorized");
        }
    }

    @Override
    public ProfileDetailResponse getProfile() {
        Integer userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        var userDto = userMapper.toProfileDetailResponse(user);
        userDto.setPhotos(getUserPhotoURL(user));
        return userDto;
    }

    @Override
    public void updateProfile(UpdateProfileRequest request) {
        Integer userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(request.getImage().getOriginalFilename()));
            fileUploadService.userPhotosUpload(request.getImage(), user.getId());
            user.setPhotos(fileName);
        }

        userRepository.save(user);
    }
}