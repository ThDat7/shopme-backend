package com.shopme.admin.controller;

import com.shopme.admin.dto.request.UpdateProfileRequest;
import com.shopme.admin.dto.request.UserCreateRequest;
import com.shopme.admin.dto.request.UserUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.service.UserService;
import com.shopme.common.dto.response.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<ListResponse<UserListResponse>> listByPage(@RequestParam Map<String, String> params) {
        ListResponse<UserListResponse> listResponse = userService.listByPage(params);
        return ApiResponse.ok(listResponse);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserDetailResponse> createUser(@ModelAttribute UserCreateRequest request) {
        return ApiResponse.ok(userService.createUser(request));
    }

    @GetMapping("/roles")
    public ApiResponse<List<RoleResponse>> listRoles() {
        return ApiResponse.ok(userService.getAllRoles());
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getUser(@PathVariable Integer id) {
        UserDetailResponse user = userService.getUserById(id);
        return ApiResponse.ok(user);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserDetailResponse> updateUser(@PathVariable Integer id, @ModelAttribute UserUpdateRequest request) {
        return ApiResponse.ok(userService.updateUser(id, request));
    }

    @PutMapping("/{id}/enable/{status}")
    public ApiResponse updateUserStatus(@PathVariable Integer id, @PathVariable boolean status) {
        userService.updateUserStatus(id, status);
        return ApiResponse.ok();
    }

    @GetMapping("/all")
    public ApiResponse<List<UserExportResponse>> listAllForExport() {
        var users = userService.listAllForExport();
        return ApiResponse.ok(users);
    }

    @GetMapping("/profile")
    public ApiResponse<ProfileDetailResponse> getProfile() {
        var user = userService.getProfile();
        return ApiResponse.ok(user);
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse updateProfile(@ModelAttribute UpdateProfileRequest request) {
        userService.updateProfile(request);
        return ApiResponse.ok();
    }
}
