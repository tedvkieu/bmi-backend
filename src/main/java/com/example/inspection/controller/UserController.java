package com.example.inspection.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inspection.dto.request.UserRequest;
import com.example.inspection.dto.response.UserResponse;
import com.example.inspection.entity.User;
import com.example.inspection.exception.AccessDeniedException;
import com.example.inspection.service.UserPermissionService;
import com.example.inspection.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserPermissionService userPermissionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        // Kiểm tra quyền tạo ADMIN
        if (request.getRole() == User.Role.ADMIN) {
            if (!userPermissionService.canCreateAdmin()) {
                throw new AccessDeniedException("Hệ thống chỉ cho phép một ADMIN duy nhất");
            }
        }

        // Kiểm tra quyền tạo user với role này
        if (!userPermissionService.canCreateUserWithRole(request.getRole())) {
            throw new AccessDeniedException("Bạn không có quyền tạo user với role này");
        }
        return ResponseEntity.ok(userService.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        UserResponse user = userService.getById(id);

        // ADMIN có thể xem tất cả, MANAGER chỉ xem được nhân viên
        if (userPermissionService.isAdmin()) {
            return ResponseEntity.ok(user);
        }

        if (userPermissionService.isManager()) {
            // Kiểm tra role của user được xem
            User.Role userRole = user.getRole();
            if (userRole == User.Role.INSPECTOR ||
                    userRole == User.Role.DOCUMENT_STAFF ||
                    userRole == User.Role.ISO_STAFF) {
                return ResponseEntity.ok(user);
            }
            throw new AccessDeniedException("Bạn chỉ có thể xem thông tin nhân viên");
        }

        throw new AccessDeniedException("Bạn không có quyền xem thông tin user này");
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        if (userPermissionService.isAdmin()) {
            return ResponseEntity.ok(userService.getAll());
        }

        if (userPermissionService.isManager()) {
            // MANAGER chỉ xem được nhân viên
            List<UserResponse> allUsers = userService.getAll();
            List<UserResponse> staffUsers = allUsers.stream()
                    .filter(user -> user.getRole() == User.Role.INSPECTOR ||
                            user.getRole() == User.Role.DOCUMENT_STAFF ||
                            user.getRole() == User.Role.ISO_STAFF)
                    .toList();
            return ResponseEntity.ok(staffUsers);
        }

        throw new AccessDeniedException("Bạn không có quyền xem danh sách user");
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserRequest request) {
        UserResponse existingUser = userService.getById(id);

        // Kiểm tra quyền thay đổi thành ADMIN
        if (request.getRole() == User.Role.ADMIN) {
            if (!userPermissionService.canCreateAdmin()) {
                throw new AccessDeniedException("Hệ thống chỉ cho phép một ADMIN duy nhất");
            }
        }

        // Kiểm tra quyền cập nhật
        if (!userPermissionService.canCreateUserWithRole(request.getRole())) {
            throw new AccessDeniedException("Bạn không có quyền cập nhật user với role này");
        }

        // Kiểm tra quyền cập nhật user hiện tại
        User.Role currentRole = existingUser.getRole();
        if (userPermissionService.isManager() &&
                currentRole != User.Role.INSPECTOR &&
                currentRole != User.Role.DOCUMENT_STAFF &&
                currentRole != User.Role.ISO_STAFF) {
            throw new AccessDeniedException("Bạn chỉ có thể cập nhật thông tin nhân viên");
        }

        return ResponseEntity.ok(userService.update(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        UserResponse existingUser = userService.getById(id);

        // Kiểm tra quyền xóa ADMIN
        if (existingUser.getRole() == User.Role.ADMIN) {
            throw new AccessDeniedException("Không thể xóa ADMIN duy nhất của hệ thống");
        }

        // Kiểm tra quyền xóa
        User.Role userRole = existingUser.getRole();
        if (userPermissionService.isManager() &&
                userRole != User.Role.INSPECTOR &&
                userRole != User.Role.DOCUMENT_STAFF &&
                userRole != User.Role.ISO_STAFF) {
            throw new AccessDeniedException("Bạn chỉ có thể xóa nhân viên");
        }

        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable String username) {
        UserResponse user = userService.getByUsername(username);

        // Kiểm tra quyền xem tương tự như getById
        if (userPermissionService.isAdmin()) {
            return ResponseEntity.ok(user);
        }

        if (userPermissionService.isManager()) {
            User.Role userRole = user.getRole();
            if (userRole == User.Role.INSPECTOR ||
                    userRole == User.Role.DOCUMENT_STAFF ||
                    userRole == User.Role.ISO_STAFF) {
                return ResponseEntity.ok(user);
            }
            throw new AccessDeniedException("Bạn chỉ có thể xem thông tin nhân viên");
        }

        throw new AccessDeniedException("Bạn không có quyền xem thông tin user này");
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getByEmail(@PathVariable String email) {
        UserResponse user = userService.getByEmail(email);

        // Kiểm tra quyền xem tương tự như getById
        if (userPermissionService.isAdmin()) {
            return ResponseEntity.ok(user);
        }

        if (userPermissionService.isManager()) {
            User.Role userRole = user.getRole();
            if (userRole == User.Role.INSPECTOR ||
                    userRole == User.Role.DOCUMENT_STAFF ||
                    userRole == User.Role.ISO_STAFF) {
                return ResponseEntity.ok(user);
            }
            throw new AccessDeniedException("Bạn chỉ có thể xem thông tin nhân viên");
        }

        throw new AccessDeniedException("Bạn không có quyền xem thông tin user này");
    }

    // API đặc biệt cho MANAGER - chỉ lấy danh sách nhân viên
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/staff")
    public ResponseEntity<List<UserResponse>> getStaffOnly() {
        if (!userPermissionService.isManager()) {
            throw new AccessDeniedException("Chỉ MANAGER mới có thể xem danh sách nhân viên");
        }

        List<UserResponse> allUsers = userService.getAll();
        List<UserResponse> staffUsers = allUsers.stream()
                .filter(user -> user.getRole() == User.Role.INSPECTOR ||
                        user.getRole() == User.Role.DOCUMENT_STAFF ||
                        user.getRole() == User.Role.ISO_STAFF)
                .toList();
        return ResponseEntity.ok(staffUsers);
    }

    // API đặc biệt cho MANAGER - tạo nhân viên mới
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/staff")
    public ResponseEntity<UserResponse> createStaff(@RequestBody UserRequest request) {
        if (!userPermissionService.isManager()) {
            throw new AccessDeniedException("Chỉ MANAGER mới có thể tạo nhân viên");
        }

        // Kiểm tra role chỉ được là nhân viên
        if (request.getRole() != User.Role.INSPECTOR &&
                request.getRole() != User.Role.DOCUMENT_STAFF &&
                request.getRole() != User.Role.ISO_STAFF) {
            throw new AccessDeniedException("Chỉ có thể tạo nhân viên với role: INSPECTOR, DOCUMENT_STAFF, ISO_STAFF");
        }

        return ResponseEntity.ok(userService.create(request));
    }

    // API đặc biệt cho ADMIN - lấy tất cả user
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<UserResponse>> getAllForAdmin() {
        if (!userPermissionService.isAdmin()) {
            throw new AccessDeniedException("Chỉ ADMIN mới có thể xem tất cả user");
        }

        return ResponseEntity.ok(userService.getAll());
    }

    // API kiểm tra trạng thái ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/status")
    public ResponseEntity<Map<String, Object>> getAdminStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("hasAdmin", userService.hasAdmin());
        status.put("canCreateAdmin", userPermissionService.canCreateAdmin());
        status.put("isOnlyAdmin", userPermissionService.isOnlyAdmin());
        return ResponseEntity.ok(status);
    }
}
