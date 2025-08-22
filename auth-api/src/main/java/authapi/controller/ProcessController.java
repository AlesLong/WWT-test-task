package authapi.controller;

import authapi.entity.ProcessingLog;
import authapi.repo.ProcessingLogRepository;
import authapi.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ProcessController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ProcessingLogRepository logRepo;
    private final JwtService jwtService;

    @Value("${INTERNAL_TOKEN:secret123}")
    private String internalToken;

    public ProcessController(ProcessingLogRepository logRepo, JwtService jwtService) {
        this.logRepo = logRepo;
        this.jwtService = jwtService;
    }

    @PostMapping("/process")
    public ResponseEntity<?> process(@RequestHeader("Authorization") String auth,
                                     @RequestBody Map<String, String> body) {
        try {
            String token = auth.replace("Bearer ", "");
            String email = jwtService.extractUsername(token);

            String text = body.get("text");

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Internal-Token", internalToken);
            headers.set("Content-Type", "application/json");

            HttpEntity<Map<String, String>> req = new HttpEntity<>(Map.of("text", text), headers);

            ResponseEntity<Map> resp = restTemplate.postForEntity(
                    "http://data-api:8080/api/transform", req, Map.class);

            String result = (String) Objects.requireNonNull(resp.getBody()).get("result");

            ProcessingLog log = new ProcessingLog();
            log.setUserId(UUID.nameUUIDFromBytes(email.getBytes()));
            log.setInputText(text);
            log.setOutputText(result);
            logRepo.save(log);

            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            e.fillInStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
