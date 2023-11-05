package ra.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import ra.security.exception.LoginException;
import ra.security.model.domain.Users;
import ra.security.model.dto.request.FormSignInDto;
import ra.security.model.dto.request.FormSignUpDto;
import ra.security.model.dto.request.FormUpdateUserDto;
import ra.security.model.dto.response.JwtResponse;
import ra.security.model.dto.response.UserResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    void updateUserInfo(String usename, FormUpdateUserDto updateUserDto) throws LoginException;

    Optional<Users> findByUserName(String username);
    List<UserResponseDTO> findByUserNameseach(String username, Pageable pageable);

    Users signup(FormSignUpDto users);
    UserResponseDTO getUserById(Long id) throws ra.security.exception.LoginException;

    void lockUserAccount(Long id) throws ra.security.exception.LoginException;

    void unlockUserAccount(Long id) throws ra.security.exception.LoginException;

    Page<UserResponseDTO> searchUsersByName(String name, Pageable pageable);
}