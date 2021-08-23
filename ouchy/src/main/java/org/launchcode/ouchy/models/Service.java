package org.launchcode.ouchy.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Service {

    public Service() {}

    @Id
    @GeneratedValue
    private int serviceId;

//    @ManyToOne
    private int providerId;

    @NotNull
    private double serviceCost;

    @NotBlank
    private String serviceName;
}
