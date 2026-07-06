package products.api.fintech.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import products.api.fintech.dto.FixedTermDepositRequest;
import products.api.fintech.dto.FixedTermDepositResponse;
import products.api.fintech.entity.FixedTermDepositEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FixedTermDepositMapper {

    FixedTermDepositResponse toResponse(FixedTermDepositEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expectedReturn", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "maturityDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    FixedTermDepositEntity toEntity(FixedTermDepositRequest request);
}
