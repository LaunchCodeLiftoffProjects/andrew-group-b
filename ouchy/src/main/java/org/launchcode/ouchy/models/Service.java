package org.launchcode.ouchy.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Service {

    public Service() {}

    public Service(String serviceName, String serviceDescription) {
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
    }

    @Id
    @GeneratedValue
    private int serviceId;

    @NotNull
    @NotBlank
    private String serviceName;

    @NotNull
    @NotBlank
    private String serviceDescription;

    public int getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
}
