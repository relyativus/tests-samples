package co.inventorsoft.unit.unittestsdemo.email.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @author anatolii vakaliuk
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailMessage {
    private Long id;
    private String receiver;
    private String subject;
    private String body;
    private Instant deliverAt;
}
