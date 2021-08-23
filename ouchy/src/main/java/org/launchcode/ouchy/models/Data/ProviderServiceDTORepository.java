package org.launchcode.ouchy.models.Data;

import org.launchcode.ouchy.models.ProviderServiceDTO;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderServiceDTORepository extends PagingAndSortingRepository<ProviderServiceDTO, Integer> {
    Iterable<ProviderServiceDTO> findAllByProviderID(int providerID);
}
