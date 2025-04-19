package Nency.project.Placement.Assistant.Controller;

import Nency.project.Placement.Assistant.model.Company;
import Nency.project.Placement.Assistant.service.GeminiExtractionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

public class JDExtractionController {
    @RestController
    @RequestMapping("/api/jd")
    public class jdextractioncontroller {

        private final GeminiExtractionService geminiService;

        public jdextractioncontroller(GeminiExtractionService geminiService) {
            this.geminiService = geminiService;
        }

        @PostMapping("/extract")
        public ResponseEntity<Company> extractCompanyDetails(@RequestParam("file") MultipartFile file) {
            try {
                Company companyDetails = geminiService.extractCompanyDetailsFromJD(file);
                return ResponseEntity.ok(companyDetails);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

}
