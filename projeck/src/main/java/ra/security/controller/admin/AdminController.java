package ra.security.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.security.exception.LoginException;
import ra.security.model.domain.Users;
import ra.security.model.dto.response.ProductResponseDTO;
import ra.security.model.dto.response.UserResponseDTO;
import ra.security.service.IUserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/use")
@CrossOrigin("*")
public class AdminController {
    @Autowired
    IUserService userService;
    @GetMapping()


    public ResponseEntity< List<UserResponseDTO>> searchUsers(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String by) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(by), sort);
        List<UserResponseDTO> userResponseDTOS = (List<UserResponseDTO>) userService.findByUserNameseach(name, pageable);
        return new ResponseEntity<>(userResponseDTOS, HttpStatus.OK);
    }
    @PutMapping("/lock/{id}")


    public ResponseEntity<String> lockUserAccount(@PathVariable Long id) throws LoginException {
        userService.lockUserAccount(id);
        return ResponseEntity.ok("Khóa use thành công");
    }
    @PutMapping("/unlock/{id}")
    public ResponseEntity<String> unlockUserAccount(@PathVariable Long id) throws LoginException {
        userService.unlockUserAccount(id);
        return ResponseEntity.ok("Mở khóa use thành công");
    }
    @GetMapping("username/{id}")

    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) throws LoginException {
        UserResponseDTO userResponse = userService.getUserById(id);
       return new ResponseEntity<>(userResponse,HttpStatus.OK);
    }
}
