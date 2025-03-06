package com.example.trainingt1.loader;

import com.example.trainingt1.entity.role.Role;
import com.example.trainingt1.entity.role.RoleEnum;
import com.example.trainingt1.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleEnum.ROLE_USER));
            roleRepository.save(new Role(RoleEnum.ROLE_ADMIN));
            roleRepository.save(new Role(RoleEnum.ROLE_MODERATOR));
        }
    }
}