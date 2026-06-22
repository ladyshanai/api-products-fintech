package products.api.fintech.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import products.api.fintech.client.AccountClient;
import products.api.fintech.client.AccountModel;
import products.api.fintech.dto.FixedTermDepositRequest;
import products.api.fintech.entity.FixedTermDepositEntity;
import products.api.fintech.enums.FixedTermDepositStatus;
import products.api.fintech.repository.FixedTermDepositRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FixedTermDepositServiceTest {

    @Mock
    private FixedTermDepositRepository repository;

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private FixedTermDepositService service;

    private AccountModel account;

    @BeforeEach
    void setUp() {
        account = new AccountModel(
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
    }

    @Test
    void shouldCreateFixedTermDeposit() {
        var request = new FixedTermDepositRequest(
                1L,
                new BigDecimal("100000.00"),
                30,
                new BigDecimal("32.50")
        );

        when(accountClient.getAccountById(1L)).thenReturn(account);

        when(repository.save(any(FixedTermDepositEntity.class)))
                .thenAnswer(invocation -> {
                    FixedTermDepositEntity entity = invocation.getArgument(0);
                    entity.setId(1L);
                    return entity;
                });

        var response = service.createFixedTermDeposit(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(1L, response.accountId());
        assertEquals(FixedTermDepositStatus.ACTIVE, response.status());
        assertTrue(response.active());
        assertEquals(new BigDecimal("100000.00"), response.amount());
        assertEquals(30, response.termInDays());
        assertEquals(new BigDecimal("102671.23"), response.expectedReturn());

        verify(accountClient).getAccountById(1L);
        verify(repository).save(any(FixedTermDepositEntity.class));
    }

    @Test
    void shouldGetFixedTermDepositById() {
        var entity = new FixedTermDepositEntity();
        entity.setId(1L);
        entity.setAccountId(1L);
        entity.setAmount(new BigDecimal("100000.00"));
        entity.setTermInDays(30);
        entity.setAnnualInterestRate(new BigDecimal("32.50"));
        entity.setExpectedReturn(new BigDecimal("102671.23"));
        entity.setStatus(FixedTermDepositStatus.ACTIVE);
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var response = service.getFixedTermDepositById(1L);

        assertEquals(1L, response.id());
        assertEquals(1L, response.accountId());
        assertEquals(FixedTermDepositStatus.ACTIVE, response.status());

        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenFixedTermDepositNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        var exception = assertThrows(
                RuntimeException.class,
                () -> service.getFixedTermDepositById(99L)
        );

        assertTrue(exception.getMessage().contains("Fixed term deposit not found"));
        verify(repository).findById(99L);
    }

    @Test
    void shouldCancelFixedTermDeposit() {
        var entity = new FixedTermDepositEntity();
        entity.setId(1L);
        entity.setAccountId(1L);
        entity.setAmount(new BigDecimal("100000.00"));
        entity.setTermInDays(30);
        entity.setAnnualInterestRate(new BigDecimal("32.50"));
        entity.setExpectedReturn(new BigDecimal("102671.23"));
        entity.setStatus(FixedTermDepositStatus.ACTIVE);
        entity.setActive(true);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(FixedTermDepositEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.cancelFixedTermDeposit(1L);

        assertEquals(FixedTermDepositStatus.CANCELLED, response.status());
        assertFalse(response.active());

        verify(repository).findById(1L);
        verify(repository).save(entity);
    }

    @Test
    void shouldDeleteFixedTermDeposit() {
        var entity = new FixedTermDepositEntity();
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.deleteById(1L);

        verify(repository).findById(1L);
        verify(repository).delete(entity);
    }
}