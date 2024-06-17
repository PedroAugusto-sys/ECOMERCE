package com.gyncomerce.ecom.controller;

import com.gyncomerce.ecom.dto.AuthenticationRequest;
import com.gyncomerce.ecom.dto.SignUpRequest;
import com.gyncomerce.ecom.dto.UserDto;
import com.gyncomerce.ecom.entity.User;
import com.gyncomerce.ecom.repository.UserRepository;
import com.gyncomerce.ecom.services.auth.AuthService;
import com.gyncomerce.ecom.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";


    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                          HttpServletResponse response) throws IOException, JSONException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Usuário ou senha incorretos.");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            response.getWriter().write(new JSONObject()
                    .put("userId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole())
                    .toString()
            );

            response.addHeader("Access-Control-Expose-Headers","Authorization");
            response.addHeader("Access-Control-Allow-Headers","Authorization,X-PINGOTHER,Origin,"+
                    "X-Requested-With,Content-Type,Accept,X-Custom-Header");

            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        }
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignUpRequest signUpRequest) {
        if (authService.hasUserWithEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Email já cadastrado", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto userDto = authService.createUser(signUpRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
