package appointmenthospital.authservice.service;

import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.model.dto.DiagnosticDTO;
import appointmenthospital.authservice.model.dto.RoleDTO;
import appointmenthospital.authservice.model.entity.Diagnostic;
import appointmenthospital.authservice.model.entity.Permission;
import appointmenthospital.authservice.model.entity.QRole;
import appointmenthospital.authservice.model.entity.Role;
import appointmenthospital.authservice.repository.RoleRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Role getEntity(long id)
    {
        return roleRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Not found"));
    }
    public RoleDTO update(RoleDTO roleDTO,Long id)
    {
        Role role=getEntity(id);
        role.setName(roleDTO.getName());
        role.setDescription(role.getDescription());
        role.setPermissions(new byte[0]);
        for(Permission p:roleDTO.getPermissions())
        {
            role.setPermissionGranted(p.getCode(),true);
        }
        return new RoleDTO( roleRepository.save(role));
    }
    public Boolean delete(Long id)
    {
        try {
            Role role=getEntity(id);
            roleRepository.delete(role);
        }catch (DataIntegrityViolationException e)
        {
            throw new AppException("Cannot delete role with "+id+" because still have account");
        }
        return true;
    }
    public RoleDTO get(Long id)
    {
        return new RoleDTO(getEntity(id));
    }
    public Page<RoleDTO> getPage(String keyword, Pageable pageable)
    {
        BooleanExpression byName=role.name.contains(keyword);
        Page<Role> page=roleRepository.findAll(byName,pageable);
        List<RoleDTO> responses=page.getContent().stream().map(RoleDTO::new).toList();
        return new PageImpl<RoleDTO>(responses,page.getPageable(),page.getTotalElements());
    }
    public RoleDTO changePermission(long id,int code,boolean value)
    {
        Role role=getEntity(id);
        role.setPermissionGranted(code,value);
        return new RoleDTO(roleRepository.save(role));
    }
}
