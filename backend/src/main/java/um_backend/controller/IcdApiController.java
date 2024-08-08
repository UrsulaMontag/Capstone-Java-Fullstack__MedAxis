package um_backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import um_backend.services.IcdApiService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/icd")
public class IcdApiController {

    private final IcdApiService icdApiService;

    @GetMapping()
    @RequestMapping("/details")
    public String getIcdDetails() {
        return icdApiService.getIcdData();
    }
}
