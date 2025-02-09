package Nency.project.Placement.Assistant.Controller;

import Nency.project.Placement.Assistant.model.Company;
import Nency.project.Placement.Assistant.repository.CompanyRepository;
import Nency.project.Placement.Assistant.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"}, allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Autowired
    private CompanyRepository companyRepository; // Add this line if you want to use the repository directly

    // Fetch all companies
    @GetMapping
    public List<Company> getAllCompanies() {
        logger.info("Fetching all companies...");
        List<Company> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            logger.warn("No companies found in the database.");
        } else {
            logger.info("Found {} companies.", companies.size());
        }
        return companies;
    }

    // Search companies by name
    @GetMapping("/search")
    public List<Company> searchCompanies(@RequestParam String name) {
        logger.info("Searching for companies with name: {}", name);
        List<Company> companies = companyService.searchCompaniesByName(name);
        if (companies.isEmpty()) {
            logger.warn("No companies found with name: {}", name);
        } else {
            logger.info("Found {} companies with name: {}", companies.size(), name);
        }
        return companies;
    }

    // Filter companies by batch
    @GetMapping("/batch/{batch}")
    public ResponseEntity<List<String>> getCompaniesByBatch(@PathVariable String batch) {
        logger.info("Fetching company names for batch: {}", batch);
        List<Company> companies = companyService.getCompaniesByBatch(batch);
        if (companies.isEmpty()) {
            logger.warn("No companies found for batch: {}", batch);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList()); // Return an empty list if no companies found
        } else {
            List<String> companyNames = companies.stream()
                    .map(Company::getName)
                    .toList(); // Extract names only
            logger.info("Found {} company names for batch: {}", companyNames.size(), batch);
            return ResponseEntity.ok(companyNames); // Return just the names
        }
    }


    // Test the MongoDB connection and get a count of companies
    @GetMapping("/test")
    public String testMongoConnection() {
        List<Company> count = companyRepository.findAll();
        return "Total companies in database: " + count.size();
    }

    // Create a new company
//    @PostMapping
//    public Company createCompany(@RequestBody Company company) {
//        logger.info("Creating new company: {}", company.getName());
//        return companyService.saveCompany(company);
//    }

    @PostMapping
    public ResponseEntity<?> createCompany(@RequestBody Company company) {
        try {
            companyRepository.save(company); // Ensure this line is saving to the database
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Company created successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to create company", "message", e.getMessage()));
        }
    }
}
