package com.shopme.admin.service;

import com.shopme.admin.dto.request.UserCreateRequest;
import com.shopme.admin.dto.request.UserUpdateRequest;
import com.shopme.admin.dto.response.*;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDetailResponse createUser(UserCreateRequest request);

    ListResponse<UserListResponse> listByPage(Map<String, String> params);

    List<RoleResponse> getAllRoles();

    void deleteUser(Integer id);

    UserDetailResponse getUserById(Integer id);

    UserDetailResponse updateUser(Integer id, UserUpdateRequest request);

    void updateUserStatus(Integer id, boolean status);

    List<UserExportResponse> listAllForExport();
}
