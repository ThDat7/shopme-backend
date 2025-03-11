package com.shopme.admin.mapper;

import com.shopme.admin.dto.response.RoleResponse;
import com.shopme.common.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

 @Mapper(componentModel = "spring")
public interface RoleMapper {
     RoleResponse toRoleResponse(Role role);

     @Named("mapRoleIds")
     default Set<Integer> mapRoleIds(Set<Role> roles) {
     if (roles == null) {
     return null;
     }
     return roles.stream()
     .map(Role::getId)
     .collect(Collectors.toSet());
     }

     @Named("mapRoleNames")
     default Set<String> mapRoleNames(Set<Role> roles) {
     if (roles == null) {
     return null;
     }
     return roles.stream()
     .map(Role::getName)
     .collect(Collectors.toSet());
     }
}
