package appointmenthospital.authservice.service;

import appointmenthospital.authservice.client.FileStorageClient;
import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.model.dto.PrescriptionDTO;
import appointmenthospital.authservice.model.entity.Examination;
import appointmenthospital.authservice.model.entity.Prescription;
import appointmenthospital.authservice.repository.PrescriptionRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private PrescriptionRepository prescriptionRepository;
    private FileStorageClient fileStorageClient;
    private ExaminationService examinationService;
    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository, FileStorageClient fileStorageClient, ExaminationService examinationService) {
        this.prescriptionRepository = prescriptionRepository;
        this.fileStorageClient = fileStorageClient;
        this.examinationService = examinationService;
    }

    public PrescriptionDTO create(long examinationId,PrescriptionDTO dto)
    {
        Examination examination=examinationService.getEntity(examinationId);
        var prescription=Prescription.builder()
                .description(dto.getDescription())
                .examination(examination)
                .build();
        return new PrescriptionDTO(prescriptionRepository.save(prescription));
    }
    public Prescription getEntity(long id)
    {
        return prescriptionRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Not found"));
    }
    public PrescriptionDTO get(long id)
    {
        return new PrescriptionDTO(getEntity(id));
    }
    public boolean delete(long id)
    {
        Prescription prescription=getEntity(id);
        try
        {
            prescriptionRepository.delete(prescription);
            return true;
        }catch (DataIntegrityViolationException e)
        {
            throw new AppException("Cannot delete");
        }
    }
    public String uploadDocument(MultipartFile file,long id)
    {
        try
        {
            Prescription prescription= getEntity(id);
            var link = fileStorageClient.uploadDocumentToFIleSystem(file);
            prescription.setDocument(link.getBody());
            prescriptionRepository.save(prescription);
            return link.getBody();
        }catch (FeignException e)
        {
            throw new AppException("Cannot upload");
        }
    }
    public String downloadDocument(long id)
    {
            Prescription prescription= getEntity(id);
            return prescription.getDocument();
    }

}
