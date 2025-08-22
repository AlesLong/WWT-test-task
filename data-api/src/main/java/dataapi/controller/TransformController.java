package dataapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TransformController {
    @Value("${INTERNAL_TOKEN:secret123}")
    private String internalToken;

    @PostMapping("/transform")
    public ResponseEntity<?> transform(@RequestHeader(value = "X-Internal-Token", required = false) String token,
                                       @RequestBody Map<String, String> body) {
        if (token == null || !token.equals(internalToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String input = body.get("text");
        if (input == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing 'text' field"));
        }
        String result = new StringBuilder(input).reverse().toString().toUpperCase();
        return ResponseEntity.ok(Map.of("result", result));

    }
}
