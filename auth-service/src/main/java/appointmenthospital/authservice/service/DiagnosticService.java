package appointmenthospital.authservice.service;

import appointmenthospital.authservice.client.FileStorageClient;
import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.DiagnosticDTO;
import appointmenthospital.authservice.model.dto.ExaminationDiagnosticDTO;
import appointmenthospital.authservice.model.entity.*;
import appointmenthospital.authservice.repository.DiagnosticRepository;
import appointmenthospital.authservice.repository.ExaminationDetailRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosticService {
    private DiagnosticRepository diagnosticRepository;
    private ExaminationDetailRepository examinationDetailRepository;
    private FileStorageClient fileStorageClient;
    private CustomLogger logger;
    private ExaminationService examinationService;
    private DoctorService doctorService;
    private final QDiagnostic diagnosticService= QDiagnostic.diagnostic;
    private final QExaminationDetail examinationDetail=QExaminationDetail.examinationDetail;
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
    public Page<ExaminationDiagnosticDTO> getPaged(Pageable pageable, Long examination_id)
    {
        BooleanExpression byExaminationID=examinationDetail.examination.id.eq(examination_id);
        Page<ExaminationDetail> examinationDetails=examinationDetailRepository.findAll(byExaminationID,pageable);
        List<ExaminationDiagnosticDTO> responses= examinationDetails.getContent().stream().map(ExaminationDiagnosticDTO::new).toList();
        return new PageImpl<ExaminationDiagnosticDTO>(responses,examinationDetails.getPageable(),examinationDetails.getTotalElements());
    }
    public ExaminationDiagnosticDTO getExamination(long diagnostic_id,long examination_id)
    {
        return new ExaminationDiagnosticDTO(getExaminationDetailEntity(diagnostic_id,examination_id));
    }
    public ExaminationDetail getExaminationDetailEntity(long diagnostic_id,long examination_id)
    {
        BooleanExpression byExaminationID=examinationDetail.examination.id.eq(examination_id);
        BooleanExpression byDiagnosticID=examinationDetail.diagnostic.id.eq(diagnostic_id);
        return examinationDetailRepository.findByDiagnosticIdAndExaminationId(diagnostic_id,examination_id).orElseThrow(()->new EntityNotFoundException("Not found"));
    }

    public ExaminationDiagnosticDTO createExamination(long diagnostic_id,long examination_id,long doctor_id)
    {
        Diagnostic diagnostic=getEntity(diagnostic_id);
        Examination examination=examinationService.getEntity(examination_id);
        Doctor doctor=doctorService.getEntity(doctor_id);
        ExaminationDetail examinationDetailEntity= ExaminationDetail.builder()
                .diagnostic(diagnostic)
                .examination(examination)
                .doctor(doctor)
                .build();
        return new ExaminationDiagnosticDTO(examinationDetailRepository.save(examinationDetailEntity));
    }
    public String uploadDocument(MultipartFile file, long diagnostic_id,long examination_id)
    {
        try
        {
            ExaminationDetail examinationDetailEntity= getExaminationDetailEntity(diagnostic_id,examination_id);
            var link = fileStorageClient.uploadDocumentToFIleSystem(file);
            examinationDetailEntity.setDocument(link.getBody());
            examinationDetailRepository.save(examinationDetailEntity);
            return link.getBody();
        }catch (FeignException e)
        {
            throw new AppException("Cannot upload");
        }
    }
    public Boolean deleteDocument( long diagnostic_id,long examination_id)
    {
        try
        {
            ExaminationDetail examinationDetailEntity= getExaminationDetailEntity(diagnostic_id,examination_id);
            var link = fileStorageClient.deleteDocumentFromFileSystem(examinationDetailEntity.getDocument());
            examinationDetailEntity.setDocument(null);
            examinationDetailRepository.save(examinationDetailEntity);
            return true;
        }catch (FeignException e)
        {
            throw new AppException("Cannot deleted");
        }
    }
}
