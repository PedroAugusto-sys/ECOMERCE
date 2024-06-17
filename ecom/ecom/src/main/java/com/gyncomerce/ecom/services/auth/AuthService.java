package com.gyncomerce.ecom.services.auth;

import com.gyncomerce.ecom.dto.SignUpRequest;
import com.gyncomerce.ecom.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignUpRequest signUpRequest);
    Boolean hasUserWithEmail(String email);
}
