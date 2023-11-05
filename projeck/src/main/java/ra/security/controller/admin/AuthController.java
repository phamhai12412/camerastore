package ra.security.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import ra.security.exception.LoginException;
import ra.security.model.dto.request.FormSignInDto;
import ra.security.model.dto.request.FormSignUpDto;
import ra.security.model.dto.response.JwtResponse;
import ra.security.security.jwt.JwtProvider;
import ra.security.security.user_principle.UserPrinciple;
import ra.security.service.IUserService;
import ra.security.service.impl.usersevice.MailService;


import javax.validation.Valid;
import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private MailService mailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("xác thực thành công");
    }
    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> signin(@RequestBody @Valid FormSignInDto formSignInDto) throws LoginException{
        Authentication authentication =null;
        try {
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(formSignInDto.getUsername(), formSignInDto.getPassword())
            );
        }catch (AuthenticationException e){
            throw new LoginException("Tài khoản hoặc mật khẩu không chính xác!");
        }
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        String token = jwtProvider.generateToken(userPrinciple);
        List<String> roles = userPrinciple.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if (!userPrinciple.isStatus()) {
            // Ném ngoại lệ LoginException khi tài khoản bị khóa
            throw new LoginException("Tài khoản bạn đã bị khóa!");
        }
        return ResponseEntity.ok(JwtResponse.builder().token(token)
                .name(userPrinciple.getName())
                .username(userPrinciple.getUsername())
                .roles(roles)
                        .type("Bearer")
                .status(userPrinciple.isStatus()).build());
    }
    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@RequestBody @Valid FormSignUpDto formSignUpDto) {

        userService.signup(formSignUpDto);
        mailService.sendEmail(formSignUpDto.getEmail(), "Đăng kí thành công", "Xin chào: " + formSignUpDto.getName() + " cảm ơn bạn đã sử dụng dịch vụ của chúng tôi");
        return new ResponseEntity<>("Đăng kí thành công", HttpStatus.CREATED);

    }
}
