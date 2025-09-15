package com.example.inspection.service;

import com.example.inspection.entity.User;
import com.example.inspection.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPermissionService {

    private final UserRepository userRepository;

    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isManager() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));
    }

    public boolean isAdminOrManager() {
        return isAdmin() || isManager();
    }

    public boolean canManageUser(User targetUser) {
        if (isAdmin()) {
            return true; // ADMIN có thể quản lý tất cả user
        }

        if (isManager()) {
            // MANAGER chỉ có thể quản lý nhân viên: INSPECTOR, DOCUMENT_STAFF, ISO_STAFF
            return targetUser.getRole() == User.Role.INSPECTOR ||
                    targetUser.getRole() == User.Role.DOCUMENT_STAFF ||
                    targetUser.getRole() == User.Role.ISO_STAFF;
        }

        return false;
    }

    public boolean canCreateUserWithRole(User.Role role) {
        if (isAdmin()) {
            return true; // ADMIN có thể tạo user với bất kỳ role nào
        }

        if (isManager()) {
            // MANAGER chỉ có thể tạo nhân viên: INSPECTOR, DOCUMENT_STAFF, ISO_STAFF
            return role == User.Role.INSPECTOR ||
                    role == User.Role.DOCUMENT_STAFF ||
                    role == User.Role.ISO_STAFF;
        }

        return false;
    }

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getDetails() instanceof Long) {
            return (Long) auth.getDetails();
        }
        return null;
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    public boolean canCreateAdmin() {
        // Kiểm tra xem đã có ADMIN nào chưa
        long adminCount = userRepository.findAll().stream()
                .filter(user -> user.getRole() == User.Role.ADMIN)
                .count();

        // Chỉ cho phép tạo ADMIN nếu chưa có ADMIN nào
        return adminCount == 0;
    }

    public boolean isOnlyAdmin() {
        // Kiểm tra xem có phải ADMIN duy nhất không
        long adminCount = userRepository.findAll().stream()
                .filter(user -> user.getRole() == User.Role.ADMIN)
                .count();

        return adminCount == 1 && isAdmin();
    }
}
