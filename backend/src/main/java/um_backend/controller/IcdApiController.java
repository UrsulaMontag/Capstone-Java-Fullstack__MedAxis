package um_backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import um_backend.clients.IcdApiClient;

@RestController
@AllArgsConstructor
@RequestMapping("/api/icd/entity")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = { "Authorization", "Content-Type", "Accept",
        "API-Version", "Accept-Language" }, allowCredentials = "true", methods = { RequestMethod.GET,
                RequestMethod.POST, RequestMethod.OPTIONS }, maxAge = 3600)
public class IcdApiController {

    private final IcdApiClient icdApiClient;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/icd/release/11/v2/mms")
    public String getIcdDetails() {
        return icdApiClient.getIcdDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/icd/release/11/mms/token")
    public String getToken() {
        return icdApiClient.getToken();
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
            @RequestParam(required = false, defaultValue = "false") boolean medicalCodingMode) {
        return icdApiClient.searchIcd(q, subtreeFilterUsesFoundationDescendants, includeKeywordResult, useFlexisearch,
                flatResults, highlightingEnabled, medicalCodingMode);
    }
}
