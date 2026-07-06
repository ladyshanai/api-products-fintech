package products.api.fintech.service;

import products.api.fintech.client.AccountClient;
import products.api.fintech.dto.FixedTermDepositRequest;
import products.api.fintech.dto.FixedTermDepositResponse;
import products.api.fintech.entity.FixedTermDepositEntity;
import products.api.fintech.enums.FixedTermDepositStatus;
import products.api.fintech.repository.FixedTermDepositRepository;
import org.springframework.stereotype.Service;
import products.api.fintech.mapper.FixedTermDepositMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FixedTermDepositService {

    private final FixedTermDepositRepository fixedTermDepositRepository;
    private final AccountClient accountClient;
    private final FixedTermDepositMapper fixedTermDepositMapper;

    public FixedTermDepositService(
            FixedTermDepositRepository fixedTermDepositRepository,
            AccountClient accountClient,
            FixedTermDepositMapper fixedTermDepositMapper
    ) {
        this.fixedTermDepositRepository = fixedTermDepositRepository;
        this.accountClient = accountClient;
        this.fixedTermDepositMapper = fixedTermDepositMapper;
    }

    public FixedTermDepositResponse createFixedTermDeposit(FixedTermDepositRequest request) {
        var account = accountClient.getAccountById(request.accountId());

        var startDate = LocalDate.now();
        var maturityDate = startDate.plusDays(request.termInDays());
        var expectedReturn = calculateExpectedReturn(
                request.amount(),
                request.annualInterestRate(),
                request.termInDays()
        );

        var entity = fixedTermDepositMapper.toEntity(request);
        entity.setAccountId(account.accountId());
        entity.setAmount(request.amount());
        entity.setTermInDays(request.termInDays());
        entity.setAnnualInterestRate(request.annualInterestRate());
        entity.setExpectedReturn(expectedReturn);
        entity.setStartDate(startDate);
        entity.setMaturityDate(maturityDate);
        entity.setStatus(FixedTermDepositStatus.ACTIVE);
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        var saved = fixedTermDepositRepository.save(entity);

        return fixedTermDepositMapper.toResponse(saved);
    }

    public List<FixedTermDepositResponse> getAllFixedTermDeposits() {
        return fixedTermDepositRepository.findAll()
                .stream()
                .map(fixedTermDepositMapper::toResponse)
                .toList();
    }

    public FixedTermDepositResponse getFixedTermDepositById(Long id) {
        var entity = fixedTermDepositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fixed term deposit not found with id: " + id));

        return fixedTermDepositMapper.toResponse(entity);
    }

    public List<FixedTermDepositResponse> getFixedTermDepositsByAccount(Long accountId) {
        return fixedTermDepositRepository.findByAccountId(accountId)
                .stream()
                .map(fixedTermDepositMapper::toResponse)
                .toList();
    }

    public List<FixedTermDepositResponse> getFixedTermDepositsByStatus(FixedTermDepositStatus status) {
        return fixedTermDepositRepository.findByStatus(status)
                .stream()
                .map(fixedTermDepositMapper::toResponse)
                .toList();
    }

    public FixedTermDepositResponse cancelFixedTermDeposit(Long id) {
        var entity = fixedTermDepositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fixed term deposit not found with id: " + id));

        entity.setStatus(FixedTermDepositStatus.CANCELLED);
        entity.setActive(false);
        entity.setUpdatedAt(LocalDateTime.now());

        var updated = fixedTermDepositRepository.save(entity);

        return fixedTermDepositMapper.toResponse(updated);
    }

    public void deleteById(Long id) {
        var entity = fixedTermDepositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fixed term deposit not found with id: " + id));

        fixedTermDepositRepository.delete(entity);
    }

    private BigDecimal calculateExpectedReturn(
            BigDecimal amount,
            BigDecimal annualInterestRate,
            Integer termInDays
    ) {
        BigDecimal interest = amount
                .multiply(annualInterestRate)
                .multiply(BigDecimal.valueOf(termInDays))
                .divide(BigDecimal.valueOf(36500), 2, RoundingMode.HALF_UP);

        return amount.add(interest);
    }
}