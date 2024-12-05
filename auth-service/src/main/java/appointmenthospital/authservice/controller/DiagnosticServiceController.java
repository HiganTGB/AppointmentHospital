package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.client.FileStorageClient;
import appointmenthospital.authservice.model.dto.DiagnosticDTO;
import appointmenthospital.authservice.model.dto.ExaminationDiagnosticDTO;
import appointmenthospital.authservice.model.entity.ExaminationDetail;
import appointmenthospital.authservice.service.DiagnosticService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/diagnostic")
@RequiredArgsConstructor
public class DiagnosticServiceController {
    private DiagnosticService diagnosticService;
    private final FileStorageClient storageClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Page", description = "Get page + search by keyword")
    @PreAuthorize("hasAuthority('ReadDiagnosticService')")
    public Page<DiagnosticDTO> getAll(@RequestParam(defaultValue = "", value = "search", required = false) String keyword,
                                      @RequestParam(defaultValue = "0", value = "page", required = false) int page,
                                      @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
                                      @RequestParam(value = "orderBy", required = false, defaultValue = "ASC") String orderBy) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return diagnosticService.getPaged(keyword, pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ReadDiagnosticService')")
    public DiagnosticDTO get(@PathVariable Long id) {
        return diagnosticService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('CreateDiagnosticService')")
    public DiagnosticDTO create(@RequestBody @Valid DiagnosticDTO dto) {
        return diagnosticService.create(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('UpdateDiagnosticService')")
    public DiagnosticDTO update(@PathVariable Long id, @RequestBody @Valid DiagnosticDTO dto) {
        return diagnosticService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('DeleteDiagnosticService')")
    public Boolean delete(@PathVariable Long id) {
        return diagnosticService.delete(id);
    }

    @GetMapping("/examination")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ReadDiagnosticService')")
    public Page<ExaminationDiagnosticDTO> getPagedExaminationDiagnostics(@RequestParam(defaultValue = "", value = "search", required = false) String keyword,
                                                                         @RequestParam(defaultValue = "0", value = "page", required = false) int page,
                                                                         @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
                                                                         @RequestParam(value = "orderBy", required = false, defaultValue = "ASC") String orderBy,
                                                                         @RequestParam(name = "examination", required = true) long examination) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort);
        return diagnosticService.getPaged(pageable, examination);
    }

    @GetMapping("/{id}/examination")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('ReadDiagnosticService')")
    public ExaminationDiagnosticDTO getExaminationDiagnostic(@PathVariable long id,
                                                             @RequestParam(name = "examination", required = true) long examination) {
        return diagnosticService.getExamination(id, examination);
    }

    @PostMapping("/{id}/examination")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasAuthority('CreateExaminationDiagnostic')")
    public ExaminationDiagnosticDTO createExaminationDiagnostic(@PathVariable long id,
                                                                @RequestParam(name = "examination", required = true) long examination,
                                                                @RequestParam(name = "doctor", required = true) long doctor) {
        return diagnosticService.createExamination(id, examination, doctor);
    }

    @PostMapping("/{id}/examination/document")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String setDocument(@PathVariable long id, @RequestPart("document") MultipartFile file, @RequestParam(name = "examination", required = true) long examination) {
        return diagnosticService.uploadDocument(file, id, examination);
    }

    @GetMapping("/{id}/examination/document")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> getDocument(@PathVariable long id, @RequestParam(name = "examination", required = true) long examination) {
        ExaminationDetail examinationDetail = diagnosticService.getExaminationDetailEntity(id,examination);
        try {
            return storageClient.downloadDocumentFromFileSystem(examinationDetail.getDocument());
        } catch (Exception e) {
            return null;
        }

    }
    @DeleteMapping("/{id}/examination/document")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean deleteDocument(@PathVariable long id, @RequestParam(name = "examination", required = true) long examination) {
        ExaminationDetail examinationDetail = diagnosticService.getExaminationDetailEntity(id,examination);
        try {
            return diagnosticService.deleteDocument(id,examination);
        } catch (Exception e) {
            return null;
        }
    }


}
