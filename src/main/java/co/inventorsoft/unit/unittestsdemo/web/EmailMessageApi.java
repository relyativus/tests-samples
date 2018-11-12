package co.inventorsoft.unit.unittestsdemo.web;

import co.inventorsoft.unit.unittestsdemo.core.ValidationResult;
import co.inventorsoft.unit.unittestsdemo.email.model.CreateEmailMessageAttributes;
import co.inventorsoft.unit.unittestsdemo.email.model.EmailMessage;
import co.inventorsoft.unit.unittestsdemo.storage.EmailStorage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author anatolii vakaliuk
 */
@AllArgsConstructor
@RestController
@RequestMapping("/emails")
public class EmailMessageApi {

    private EmailStorage emailStorage;

    @PostMapping
    public ResponseEntity create(@RequestBody Map<String, Object> params) {
        final CreateEmailMessageAttributes emailMessageAttributes = new CreateEmailMessageAttributes(params);
        final ValidationResult<EmailMessage> validationResult = emailMessageAttributes.validate();
        if (validationResult.isValid()) {
            return ResponseEntity.ok(emailStorage.create(validationResult.getObject()));
        } else {
            return ResponseEntity.badRequest().body(validationResult.getErrors());
        }

    }
}
