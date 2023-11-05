package ra.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.security.model.domain.Category;
import ra.security.model.domain.RoleName;
import ra.security.model.domain.Users;
import ra.security.model.dto.response.UserResponseDTO;

import java.util.List;
import java.util.Optional;
@Repository
public interface IUserRepository extends JpaRepository<Users,Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail( String email);
    boolean existsByPhone(String phone);
    Optional<Users> findByUsername(String username);
    // Tìm tất cả người dùng có vai trò "USER"
    @Query("SELECT u FROM Users u JOIN u.roles r WHERE r.roleName = :roleName")
    Page<Users> findAllUsersWithUserRole(RoleName roleName,Pageable pageable);
    Page<Users> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

}