package com.example.leave_app.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.leave_app.entity.permission.Permission;

import java.util.Set;
import java.util.stream.Collectors;
//import java.security.Permission;
import java.util.List;

//@RequiredArgsConstructor
public enum Role {

        ADMIN(
                        Set.of(
                                        Permission.ADMIN_READ,
                                        Permission.ADMIN_UPDATE,
                                        Permission.ADMIN_DELETE,
                                        Permission.ADMIN_CREATE)),
        USER(
                        Set.of(
                                        Permission.USER_READ,
                                        Permission.USER_UPDATE,
                                        Permission.USER_DELETE,
                                        Permission.USER_CREATE)),
        MANAGER(
                        Set.of(
                                        Permission.MANAGER_READ,
                                        Permission.MANAGER_UPDATE,
                                        Permission.MANAGER_DELETE,
                                        Permission.MANAGER_CREATE));

        private final Set<Permission> permissions;

        Role(Set<Permission> permissions) {
                this.permissions = permissions;
        }

        public List<SimpleGrantedAuthority> getAuthorities() {
                var authorities = getPermissions()
                                .stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                                .collect(Collectors.toList());
                System.out.println("authorities: " + authorities);
                authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
                return authorities;
        }

        public Set<Permission> getPermissions() {
                return permissions;
        }
}