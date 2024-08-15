package um_backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import um_backend.services.IcdApiService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/icd/entity")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = {"Authorization", "Content-Type", "Accept", "API-Version", "Accept-Language"},
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS},
        maxAge = 3600
)
public class IcdApiController {

    private final IcdApiService icdApiService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/icd/release/11/v2/mms")
    public String getIcdDetails() {
        return icdApiService.getIcdData();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/icd/release/11/mms/token")
    public String getToken() {
        return icdApiService.getToken();
    }

    @ResponseStatus(HttpStatus.OK)
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/icd/release/11/v2/mms/{code}")
    public String getIcdDetails(@PathVariable String code) {
        String uri = "https://id.who.int/icd/entity/" + code;
        return icdApiService.getIcdData(uri);
    }
}
