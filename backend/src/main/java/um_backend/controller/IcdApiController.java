package um_backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{code}")
    public String getIcdDetails(@PathVariable String code) {
        String uri = "https://id.who.int/icd/entity/" + code;
        return icdApiService.getIcdData(uri);
    }
}
