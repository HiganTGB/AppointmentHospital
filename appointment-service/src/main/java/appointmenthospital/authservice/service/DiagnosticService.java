package appointmenthospital.authservice.service;

import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.DiagnosticDTO;
import appointmenthospital.authservice.model.entity.Diagnostic;
import appointmenthospital.authservice.model.entity.QDiagnostic;
import appointmenthospital.authservice.repository.DiagnosticRepository;
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
public class DiagnosticService {
    private DiagnosticRepository diagnosticRepository;
    private CustomLogger logger;
    private final QDiagnostic diagnosticService= QDiagnostic.diagnostic;
    public Page<DiagnosticDTO> getPaged(String keyword, Pageable pageable)
    {
        BooleanExpression byName=diagnosticService.name.contains(keyword);
        Page<Diagnostic> diagnosticServicePage=diagnosticRepository.findAll(byName,pageable);
        List<DiagnosticDTO> responses=diagnosticServicePage.getContent().stream().map(DiagnosticDTO::new).toList();
        return new PageImpl<DiagnosticDTO>(responses,diagnosticServicePage.getPageable(),diagnosticServicePage.getTotalElements());
    }
    public Diagnostic getEntity(long id)
    {
        return diagnosticRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Diagnostic with id not found"));
    }
    public DiagnosticDTO get(long id)
    {
        return new DiagnosticDTO(getEntity(id));
    }
    public DiagnosticDTO create(DiagnosticDTO dto)
    {
        Diagnostic diagnostic = Diagnostic.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .build();
        return new DiagnosticDTO(diagnosticRepository.save(diagnostic));
    }
    public DiagnosticDTO update(DiagnosticDTO dto, long id)
    {
        Diagnostic diagnostic =getEntity(id);
        diagnostic.setName(dto.getName());
        diagnostic.setPrice(dto.getPrice());
        return new DiagnosticDTO(diagnosticRepository.save(diagnostic));
    }
    public Boolean delete(long id)
    {
        Diagnostic diagnostic =getEntity(id);
        try {
            diagnosticRepository.delete(diagnostic);
        }catch (DataIntegrityViolationException e)
        {
            throw new AppException("Cannot delete diagnosticService with "+id+" because still have Examination");
        }
        return true;
    }
}
