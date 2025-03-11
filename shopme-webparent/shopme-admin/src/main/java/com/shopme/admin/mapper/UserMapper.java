package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.UserCreateRequest;
import com.shopme.admin.dto.response.UserDetailResponse;
import com.shopme.admin.dto.response.UserExportResponse;
import com.shopme.admin.dto.response.UserListResponse;
import com.shopme.common.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleNames")
    UserListResponse toUserListResponse(User user);

    @Mapping(target = "roleIds", source = "roles", qualifiedByName = "mapRoleIds")
    UserDetailResponse toUserDetailResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserCreateRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleNames")
    UserExportResponse toUserExportResponse(User user);
}
