package ra.security.service.impl.usersevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.security.exception.MyCustomRuntimeException;
import ra.security.model.domain.Role;
import ra.security.model.domain.RoleName;
import ra.security.repository.IRoleRepository;
import ra.security.service.IRoleService;
@Service
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;
    @Override
    public Role findByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(()->new MyCustomRuntimeException("Không tìm thấy role"));
    }
}
