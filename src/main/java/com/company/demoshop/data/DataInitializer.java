package com.company.demoshop.data;

import com.company.demoshop.model.Role;
import com.company.demoshop.model.User;
import com.company.demoshop.repository.RoleRepository;
import com.company.demoshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN","ROLE_USER");
        createDefaultUserIfNotExist();
        createDefaultRoleIfNotExist(defaultRoles);
       createDefaultAdminIfNotExist();
    }

    private void createDefaultUserIfNotExist(){
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for(int i=0;i<=5;i++){
            String defaultEmail = "user"+i+"@email.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user  = new User();
            user.setFirstName("User");
            user.setLastName("user"+i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default user "+i+" created successfully");

        }
    }

    private void createDefaultAdminIfNotExist(){
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        for(int i=0;i<=5;i++){
            String defaultEmail = "admin"+i+"@email.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user  = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin"+i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default Admin user "+i+" created successfully");

        }
    }

    private void createDefaultRoleIfNotExist(Set<String> roles){
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }

}
