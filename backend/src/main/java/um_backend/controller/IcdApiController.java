package um_backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import um_backend.services.IcdApiService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/icd/entity")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST})
public class IcdApiController {

    private final IcdApiService icdApiService;

    @GetMapping("/icd/release/11/v2/mms")
    public String getIcdDetails() {
        return icdApiService.getIcdData();
    }

    @GetMapping("/icd/release/11/mms/token")
    public String getToken() {
        return icdApiService.getToken();
    }

    @PostMapping("icd/release/11/v2/mms/search")
    public String searchIcd(
            @RequestParam String q,
            @RequestParam(required = false, defaultValue = "false") boolean subtreeFilterUsesFoundationDescendants,
            @RequestParam(required = false, defaultValue = "false") boolean includeKeywordResult,
            @RequestParam(required = false, defaultValue = "false") boolean useFlexisearch,
            @RequestParam(required = false, defaultValue = "false") boolean flatResults,
            @RequestParam(required = false, defaultValue = "false") boolean highlightingEnabled,
            @RequestParam(required = false, defaultValue = "false") boolean medicalCodingMode
    ) {
        return icdApiService.searchIcd(q, subtreeFilterUsesFoundationDescendants, includeKeywordResult, useFlexisearch, flatResults, highlightingEnabled, medicalCodingMode);
    }

    @GetMapping("/icd/release/11/2024-01/mms/search")
    public String getIcd(
            @RequestParam String q,
            @RequestParam(required = false, defaultValue = "false") boolean subtreeFilterUsesFoundationDescendants,
            @RequestParam(required = false, defaultValue = "false") boolean includeKeywordResult,
            @RequestParam(required = false, defaultValue = "true") boolean useFlexisearch,
            @RequestParam(required = false, defaultValue = "false") boolean flatResults,
            @RequestParam(required = false, defaultValue = "false") boolean highlightingEnabled,
            @RequestParam(required = false, defaultValue = "false") boolean medicalCodingMode
    ) {
        return icdApiService.searchIcd(q, subtreeFilterUsesFoundationDescendants, includeKeywordResult, useFlexisearch, flatResults, highlightingEnabled, medicalCodingMode);
    }

    @GetMapping("/icd/release/11/v2/mms/{code}")
    public String getIcdDetails(@PathVariable String code) {
        String uri = "https://id.who.int/icd/entity/" + code;
        return icdApiService.getIcdData(uri);
    }
}
