package com.example.leave_app.entity;

//import org.springframework.security.core.authority.SimpleGrantedAuthority;

// import com.example.leave_app.entity.permission.Permission;

// import lombok.Getter;
// import lombok.RequiredArgsConstructor;

// import java.util.Set;
// import java.util.stream.Collectors;
// import java.util.List;

// @RequiredArgsConstructor
public enum Role {
    USER,
    ADMIN,
    MANAGER

    // @Getter
    // private final Set<Permission> permissions;

    // public List<SimpleGrantedAuthority> getGrantedAuthorities() {
    // List<SimpleGrantedAuthority> permissions = getPermissions().stream()
    // .map(permission -> new SimpleGrantedAuthority(permission.name()))
    // .collect(Collectors.toList());
    // permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    // return permissions;
    // }
}