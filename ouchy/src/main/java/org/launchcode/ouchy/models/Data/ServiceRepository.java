package org.launchcode.ouchy.models.Data;

import org.launchcode.ouchy.models.Provider;
import org.launchcode.ouchy.models.Service;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends PagingAndSortingRepository<Service, Integer> {
    Iterable<Service> findByOrderByServiceNameAsc();

    Iterable<Service> findByOrderByServiceNameDesc();
}
