package co.inventorsoft.unit.unittestsdemo.web;

import co.inventorsoft.unit.unittestsdemo.email.model.EmailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author anatolii vakaliuk
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EmailMessageApiTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private JdbcOperations jdbcOperations;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    @Test
    public void createShouldCreateEmailMessageAndReturnSuccessResponseWithEmailMessageAsBodyInJsonFormat() throws Exception {
        final EmailMessage expectedEmailMessage = new EmailMessage(null, "test@gmail.com",
                "Hello", "I'm body", Instant.ofEpochSecond(1542062745).plus(1, ChronoUnit.DAYS));
        final byte[] bodyJson = objectMapper.writeValueAsBytes(
                Map.of("receiver", expectedEmailMessage.getReceiver(), "subject",
                        expectedEmailMessage.getSubject(), "body",
                        expectedEmailMessage.getBody(),
                        "deliverAt", expectedEmailMessage.getDeliverAt())
        );
        var resultMatcher = mockMvc.perform(post("/emails")
                .content(bodyJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        final MvcResult mvcResult = resultMatcher
                .andExpect(status().is(200))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        final byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();
        final EmailMessage actualBody = objectMapper.readValue(contentAsByteArray, EmailMessage.class);

        assertEquals(expectedEmailMessage, actualBody);
        checkDbRecord(expectedEmailMessage);
    }

    private void checkDbRecord(EmailMessage expectedEmailMessage) {
        ResultSetExtractor<Long> totalExtractor = rs -> {
            rs.next();
            return rs.getLong("total");
        };
        final PreparedStatementSetter preparedStatementSetter = stm -> {
            stm.setString(1, expectedEmailMessage.getReceiver());
            stm.setString(2, expectedEmailMessage.getSubject());
            stm.setString(3, expectedEmailMessage.getBody());
            stm.setTimestamp(4, Timestamp.from(expectedEmailMessage.getDeliverAt()));
        };
        long total = jdbcOperations.query("SELECT count(*) as total FROM pending_emails pe WHERE pe.receiver = ?" +
                        " AND pe.subject = ? AND pe.body = ? AND pe.deliver_at = ?", preparedStatementSetter,
                totalExtractor);

        assertEquals(1L, total);
    }
}