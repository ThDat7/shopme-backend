package com.shopme.admin.controller;

import com.shopme.admin.dto.request.StateCreateRequest;
import com.shopme.admin.dto.request.StateUpdateRequest;
import com.shopme.admin.dto.response.StateDetailResponse;
import com.shopme.admin.dto.response.StateListResponse;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.service.StateService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;

    @GetMapping
    public ApiResponse<ListResponse<StateListResponse>> listByPage(@RequestParam Map<String, String> params) {
        ListResponse<StateListResponse> listResponse = stateService.listByPage(params);
        return ApiResponse.ok(listResponse);
    }

    @PostMapping
    public ApiResponse<StateDetailResponse> createState(@RequestBody StateCreateRequest request) {
        return ApiResponse.ok(stateService.createState(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteState(@PathVariable Integer id) {
        stateService.deleteState(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<StateDetailResponse> getState(@PathVariable Integer id) {
        StateDetailResponse state = stateService.getStateById(id);
        return ApiResponse.ok(state);
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<StateDetailResponse> updateState(@PathVariable Integer id,
                                                        @RequestBody StateUpdateRequest request) {
        return ApiResponse.ok(stateService.updateState(id, request));
    }
}
