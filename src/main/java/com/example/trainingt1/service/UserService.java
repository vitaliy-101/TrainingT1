package com.example.trainingt1.service;

import com.example.trainingt1.dto.LoginResponse;
import com.example.trainingt1.entity.role.Role;
import com.example.trainingt1.entity.User;
import com.example.trainingt1.entity.role.RoleEnum;
import com.example.trainingt1.exceptions.ExistByLoginException;
import com.example.trainingt1.exceptions.NotFoundByRoleException;
import com.example.trainingt1.repository.RoleRepository;
import com.example.trainingt1.repository.UserRepository;
import com.example.trainingt1.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public boolean existByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    private void setUserRole(User user, List<RoleEnum> roleEnums) {
        Set<Role> roles = new HashSet<>();
        if (roleEnums == null || roleEnums.isEmpty()) {
            setDefaultRole(user, roles);
            return;
        }
        for (RoleEnum roleEnum : roleEnums) {
            roles.add(roleRepository.findByName(roleEnum)
                    .orElseThrow(() -> new NotFoundByRoleException(Role.class, roleEnum)));
        }
        user.setRoles(roles);
    }

    private void setDefaultRole(User user, Set<Role> roles) {
        roles.add(roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new NotFoundByRoleException(Role.class, RoleEnum.ROLE_USER)));
        user.setRoles(roles);
    }


    public String register(User user, List<RoleEnum> roleEnums) {
        if (existByLogin(user.getLogin())) {
            throw new ExistByLoginException(User.class, user.getLogin());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        setUserRole(user, roleEnums);
        userRepository.save(user);
        return "Success";
    }

    public LoginResponse authenticate(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new LoginResponse(
                jwt,
                userDetails.getUsername(),
                userDetails.getId(),
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
    }
}
