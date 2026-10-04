package org.vivek.module5.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vivek.module5.dto.LoginDto;
import org.vivek.module5.dto.SignUpDto;
import org.vivek.module5.dto.UserDto;
import org.vivek.module5.entity.UserEntity;
import org.vivek.module5.exceptions.ResourceNotFoundException;
import org.vivek.module5.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + username + " not found."));
    }

    public UserDto signUp(SignUpDto signUpDto) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(signUpDto.getEmail());
        if (userEntity.isPresent()){
            throw new BadCredentialsException("User with email already exists" + signUpDto.getEmail());
        }

        UserEntity toBeCreated = modelMapper.map(signUpDto, UserEntity.class );
        toBeCreated.setPassword(passwordEncoder.encode(toBeCreated.getPassword()));

        UserEntity savedUser = userRepository.save(toBeCreated);

        return modelMapper.map(savedUser, UserDto.class);
    }
}
