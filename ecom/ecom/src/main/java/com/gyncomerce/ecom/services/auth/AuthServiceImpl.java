package com.gyncomerce.ecom.services.auth;

import com.gyncomerce.ecom.dto.SignUpRequest;
import com.gyncomerce.ecom.dto.UserDto;
import com.gyncomerce.ecom.entity.User;
import com.gyncomerce.ecom.enums.UserRole;
import com.gyncomerce.ecom.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setPassword(bCryptPasswordEncoder.encode(signUpRequest.getPassword())); // Codifica a senha
        user.setRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());

        return userDto;
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount() {
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);
        if (adminAccount == null) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("administrador");
            user.setRole(UserRole.ADMIN);
            user.setPassword(bCryptPasswordEncoder.encode("admin")); // Codifica a senha
            userRepository.save(user);
        }
    }
}
