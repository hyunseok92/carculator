package com.example.carculator.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/email/{email}")
    public ResponseEntity<String> sendEmail(@PathVariable("email") String email) throws Exception {
        try {

            String code = emailService.sendEmail(email);
            return ResponseEntity.ok(code);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PostMapping("/forgotpassword/{email}")
    public ResponseEntity<String> forgotpassword(@PathVariable("email") String email) throws Exception {
        try {
            String code = emailService.forgotPwd(email);

            log.info("forgotemail" + code);

            return ResponseEntity.ok(code);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

}
