package products.api.fintech.e2e;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import products.api.fintech.client.AccountClient;
import products.api.fintech.client.AccountModel;
import products.api.fintech.repository.FixedTermDepositRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FixedTermDepositControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FixedTermDepositRepository repository;

    @MockitoBean
    private AccountClient accountClient;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        var account = new AccountModel(
                1L,
                1L,
                "Juan",
                "1001-001-ARS",
                "ARS",
                new BigDecimal("50000.00"),
                BigDecimal.ZERO,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(accountClient.getAccountById(anyLong())).thenReturn(account);
    }

    @Test
    void shouldCreateFixedTermDeposit() throws Exception {
        String request = getRequestBody();

        mockMvc.perform(post("/api/v1/fixed-term-deposits")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.amount").value(100000.00))
                .andExpect(jsonPath("$.termInDays").value(30))
                .andExpect(jsonPath("$.annualInterestRate").value(32.50))
                .andExpect(jsonPath("$.expectedReturn").value(102671.23))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldGetAllFixedTermDeposits() throws Exception {
        createFixedTermDeposit();

        mockMvc.perform(get("/api/v1/fixed-term-deposits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldGetFixedTermDepositById() throws Exception {
        Long id = createFixedTermDeposit();

        mockMvc.perform(get("/api/v1/fixed-term-deposits/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldGetFixedTermDepositsByAccount() throws Exception {
        createFixedTermDeposit();

        mockMvc.perform(get("/api/v1/fixed-term-deposits/account/{accountId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountId").value(1));
    }

    @Test
    void shouldCancelFixedTermDeposit() throws Exception {
        Long id = createFixedTermDeposit();

        mockMvc.perform(patch("/api/v1/fixed-term-deposits/{id}/cancel", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void shouldDeleteFixedTermDeposit() throws Exception {
        Long id = createFixedTermDeposit();

        mockMvc.perform(delete("/api/v1/fixed-term-deposits/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/fixed-term-deposits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private Long createFixedTermDeposit() throws Exception {
        String response = mockMvc.perform(post("/api/v1/fixed-term-deposits")
                        .contentType(APPLICATION_JSON)
                        .content(getRequestBody()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Long.valueOf(response.replaceAll(".*\"id\":(\\d+).*", "$1"));
    }

    private String getRequestBody() {
        return """
                {
                  "accountId": 1,
                  "amount": 100000.00,
                  "termInDays": 30,
                  "annualInterestRate": 32.50
                }
                """;
    }
}