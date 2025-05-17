package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.client.FileStorageClient;
import appointmenthospital.authservice.model.dto.ExaminationDTO;
import appointmenthospital.authservice.model.dto.PrescriptionDTO;
import appointmenthospital.authservice.model.dto.RoleDTO;
import appointmenthospital.authservice.model.entity.Examination;
import appointmenthospital.authservice.service.ExaminationService;
import appointmenthospital.authservice.service.PrescriptionService;
import com.netflix.discovery.converters.Auto;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/examination")
public class ExaminationController {
    private ExaminationService examinationService;
    private PrescriptionService prescriptionService;
    private final FileStorageClient storageClient;
    @Autowired
    public ExaminationController(ExaminationService examinationService, PrescriptionService prescriptionService, FileStorageClient storageClient) {
        this.examinationService = examinationService;
        this.prescriptionService = prescriptionService;
        this.storageClient = storageClient;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Page", description = "Get page + search by keyword")
    public Page<ExaminationDTO> getAll(@RequestParam(defaultValue = "",value = "search",required =false) String keyword,
                                       @RequestParam(defaultValue = "0",value = "page",required =false)int page,
                                       @RequestParam(value="sortBy" ,required = false,defaultValue = "id") String sortBy,
                                       @RequestParam(value="orderBy" ,required = false,defaultValue = "ASC") String orderBy,
                                       @RequestParam(value="doctor" ,required = false,defaultValue = "id") Long doctor_id

    ) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return examinationService.getPaged(pageable,doctor_id);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ExaminationDTO get(@PathVariable Long id)
    {
        return  examinationService.get(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ExaminationDTO create(@RequestBody @Valid ExaminationDTO DTO)
    {
        return  examinationService.create(DTO);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ExaminationDTO update(@PathVariable Long id,@RequestBody @Valid ExaminationDTO DTO)
    {
        return  examinationService.update(DTO,id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean delete(@PathVariable Long id)
    {
        return  examinationService.delete(id);
    }
    @GetMapping("/{id}/perscription")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PrescriptionDTO get(@PathVariable long id)
    {
        Examination examination=examinationService.getEntity(id);
        return new PrescriptionDTO(examination.getPrescription());
    }
    @PostMapping("/{id}/perscription")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PrescriptionDTO create(@PathVariable long id,@RequestBody @Valid PrescriptionDTO DTO)
    {
        return  prescriptionService.create(id,DTO);
    }
    @DeleteMapping("/{id}/perscription")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean delete(@PathVariable long id)
    {

        Examination examination=examinationService.getEntity(id);
        return  prescriptionService.delete(examination.getPrescription().getId());
    }
    @GetMapping("/{id}/perscription/document")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> getDocument(@PathVariable long id)
    {
        Examination examination=examinationService.getEntity(id);
        try
        {
            return storageClient.downloadDocumentFromFileSystem(examination.getPrescription().getDocument());
        }catch (Exception e)
        {
            return null;
        }

    }
    @PostMapping("/{id}/perscription/document")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String setDocument(@PathVariable long id,@RequestPart("document") MultipartFile file)
    {
        Examination examination=examinationService.getEntity(id);
        return prescriptionService.uploadDocument(file,examination.getPrescription().getId());
    }


}
