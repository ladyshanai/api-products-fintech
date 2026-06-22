package products.api.fintech.repository;

import products.api.fintech.entity.FixedTermDepositEntity;
import products.api.fintech.enums.FixedTermDepositStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixedTermDepositRepository extends JpaRepository<FixedTermDepositEntity, Long> {

    List<FixedTermDepositEntity> findByAccountId(Long accountId);

    List<FixedTermDepositEntity> findByStatus(FixedTermDepositStatus status);

    List<FixedTermDepositEntity> findByActive(Boolean active);
}