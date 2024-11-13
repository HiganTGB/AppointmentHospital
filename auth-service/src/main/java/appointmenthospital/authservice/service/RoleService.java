package appointmenthospital.authservice.service;

import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.model.dto.RoleDTO;
import appointmenthospital.authservice.model.entity.Permission;
import appointmenthospital.authservice.model.entity.QRole;
import appointmenthospital.authservice.model.entity.Role;
import appointmenthospital.authservice.repository.RoleRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final QRole role=QRole.role;
    public RoleDTO create(RoleDTO roleDTO)
    {
        Role role=new Role();
        role.setName(roleDTO.getName());
        role.setDescription(role.getDescription());
        for(Permission p:roleDTO.getPermissions())
        {
            role.setPermissionGranted(p.getCode(),true);
        }
        return new RoleDTO( roleRepository.save(role));
    }
    public RoleDTO update(RoleDTO roleDTO,Long id)
    {
        Role role=roleRepository.getReferenceById(id);
        role.setName(roleDTO.getName());
        role.setDescription(role.getDescription());
        role.setPermissionsString(new byte[0]);
        for(Permission p:roleDTO.getPermissions())
        {
            role.setPermissionGranted(p.getCode(),true);
        }
        return new RoleDTO( roleRepository.save(role));
    }
    public Boolean delete(Long id)
    {
        try {
            Role role=roleRepository.getReferenceById(id);
            roleRepository.delete(role);
        }catch (DataIntegrityViolationException e)
        {
            throw new AppException("Cannot delete role with "+id+" because still have account");
        }
        return true;
    }
    public RoleDTO get(Long id)
    {
        return new RoleDTO(roleRepository.getReferenceById(id));
    }
    public Page<Role> getPage(String keyword, Pageable pageable)
    {
        BooleanExpression byName=role.name.contains(keyword);
        return roleRepository.findAll(byName,pageable);
    }
}
