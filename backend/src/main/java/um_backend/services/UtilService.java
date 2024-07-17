package um_backend.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilService {
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
