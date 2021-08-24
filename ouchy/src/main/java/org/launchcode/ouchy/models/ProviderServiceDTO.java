package org.launchcode.ouchy.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
public class ProviderServiceDTO {
    public ProviderServiceDTO() {}

    public ProviderServiceDTO(int serviceID, int providerID, Double serviceCost, String serviceName) {
        this.serviceID = serviceID;
        this.providerID = providerID;
        this.serviceCost = serviceCost;
        this.serviceName = serviceName;
    }

    @Id
    @GeneratedValue
    private int ProviderServiceDTOID;

    @NotNull
    private int serviceID;

    @NotNull
    private int providerID;

    @NotNull
    private Double serviceCost;

    @NotNull
    private String serviceName;

    public int getProviderServiceDTOID() {
        return ProviderServiceDTOID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public int getProviderID() {
        return providerID;
    }

    public Double getServiceCost() {
        return serviceCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceCost(Double serviceCost) {
        this.serviceCost = serviceCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProviderServiceDTO that = (ProviderServiceDTO) o;
        return serviceID == that.serviceID && providerID == that.providerID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceID, providerID);
    }
}
