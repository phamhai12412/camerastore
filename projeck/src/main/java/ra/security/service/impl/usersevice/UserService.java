package ra.security.service.impl.usersevice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.security.exception.LoginException;
import ra.security.exception.UsernameAlreadyExistsException;
import ra.security.model.domain.Role;
import ra.security.model.domain.RoleName;
import ra.security.model.domain.Users;

import ra.security.model.dto.request.FormSignUpDto;

import ra.security.model.dto.request.FormUpdateUserDto;
import ra.security.model.dto.response.UserResponseDTO;
import ra.security.repository.IUserRepository;

import ra.security.service.IRoleService;
import ra.security.service.IUserService;


import java.util.*;



@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;






    @Override
    public Optional<Users> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
    @Autowired
    private IRoleService roleService;
    @Override
    public List<UserResponseDTO> findByUserNameseach(String username, Pageable pageable) {
        Page<Users> userPage = userRepository.findAllByNameContainingIgnoreCase(username, pageable);

        if (username.isEmpty()) {
            Page<Users> usersPage = userRepository.findAllUsersWithUserRole(RoleName.ROLE_USER, pageable);
            List<Users> usersList = usersPage.getContent();
            return convertToUserResponseDTO(usersList);
        }

        List<Users> usersList = userPage.getContent();
        return convertToUserResponseDTO(usersList);
    }

    private List<UserResponseDTO> convertToUserResponseDTO(List<Users> usersList) {
        List<UserResponseDTO> userResponseDTOs = new ArrayList<>();

        for (Users user : usersList) {
            if (!hasRoleAdmin(user)) {
                UserResponseDTO userResponseDTO = convertToUserResponseDTO(user);
                userResponseDTOs.add(userResponseDTO);
            }
        }

        return userResponseDTOs;
    }

    private boolean hasRoleAdmin(Users user) {
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().equals(RoleName.ROLE_ADMIN)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UserResponseDTO getUserById(Long id) throws LoginException {
        Optional<Users> userOptional = userRepository.findById(id);
        Users user=null;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (hasRoleAdmin(user)) {
                throw new LoginException("không tìm thấy use");
            }





        }
        return convertToUserResponseDTO(user);
    }

    @Override
    public void lockUserAccount(Long id) throws LoginException {
        Users user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new LoginException("Không tìm thấy người dùng");
        }

        if (hasRoleAdmin(user)) {
            throw new LoginException("Không tìm thấy người dùng");
        } else {
            user.setStatus(false);
            userRepository.save(user);
        }
    }

    @Override
    public void unlockUserAccount(Long id) throws LoginException {
        Users user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new LoginException("Không tìm thấy người dùng");
        }

        if (hasRoleAdmin(user)) {
            throw new LoginException("Không tìm thấy người dùng");
        } else {
            user.setStatus(true);
            userRepository.save(user);
        }
    }

    @Override
    public Page<UserResponseDTO> searchUsersByName(String name, Pageable pageable) {
        Page<Users> usersPage = userRepository.findAllByNameContainingIgnoreCase(name, pageable);
        usersPage = (Page<Users>) usersPage.filter(user -> !isAdmin(user));

        return usersPage.map(this::convertToUserResponseDTO);
    }

    private boolean isAdmin(Users user) {
        user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("ROLE_ADMIN"));
        return false;
    }


    private UserResponseDTO convertToUserResponseDTO(Users user) {
        return UserResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .username(user.getUsername())
                .phone(user.getPhone())
                .status(user.isStatus())
                .build();
    }
    @Override
    public Users signup(FormSignUpDto form) {
        if (userRepository.existsByUsername(form.getUsername())){
            throw new UsernameAlreadyExistsException("Use name đã tồn tại");
        }
        if(userRepository.existsByEmail( form.getEmail())){
            throw new UsernameAlreadyExistsException("Bạn đã có tài khoản");
        }
        if(userRepository.existsByPhone( form.getPhone())){
            throw new UsernameAlreadyExistsException("Bạn đã có tài khoản");
        }
        // lấy ra danh sách các quyền và chuyển thành đối tượng Users
        Set<Role> roles  = new HashSet<>();
        roles.add(roleService.findByRoleName(RoleName.ROLE_USER));
        Users users = Users.builder()
                .name(form.getName())
                .username(form.getUsername())
                .phone(form.getPhone())
                .email(form.getEmail())
                .address(form.getAddress())
                .password(passwordEncoder.encode(form.getPassword()))
                .status(true)
                .roles(roles)
                .build();

        return userRepository.save(users);
    }
    @Override
    public void updateUserInfo(String usename, FormUpdateUserDto updateUserDto) throws LoginException{
        Users user = userRepository.findByUsername(usename).orElse(null);

        if (user == null) {
            throw new LoginException("Không tìm thấy người dùng");
        }
        if(userRepository.existsByEmail( updateUserDto.getEmail())&&!updateUserDto.getEmail().equals(user.getEmail())){
            throw new UsernameAlreadyExistsException("Email đã được đăng kí");
        }
        if(userRepository.existsByPhone( updateUserDto.getPhone())&&!updateUserDto.getPhone().equals(user.getPhone())){
            throw new UsernameAlreadyExistsException("Số điện thoại đã được đăng kí");
        }
        // Cập nhật thông tin người dùng
        if(updateUserDto.getName()!=null){
            user.setName(updateUserDto.getName());
        }
        if(updateUserDto.getPhone()!=null){
            user.setPhone(updateUserDto.getPhone());
        }
        if(updateUserDto.getEmail()!=null){
            user.setEmail(updateUserDto.getEmail());
        }
if(updateUserDto.getAddress()!=null){
    user.setAddress(updateUserDto.getAddress());
}


        userRepository.save(user);
    }


}