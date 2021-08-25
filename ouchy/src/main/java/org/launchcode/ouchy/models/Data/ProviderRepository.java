package org.launchcode.ouchy.models.Data;

import org.launchcode.ouchy.models.Provider;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends PagingAndSortingRepository<Provider, Integer> {
}
