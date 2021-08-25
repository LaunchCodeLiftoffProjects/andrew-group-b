package org.launchcode.ouchy.models;

import org.launchcode.ouchy.models.Data.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
public class Provider {

    public Provider () {}

    public Provider(String providerName, String providerAddress, String providerPhone, String providerEmail) {
        this.providerName = providerName;
        this.providerAddress = providerAddress;
        this.providerPhone = providerPhone;
        this.providerEmail = providerEmail;
    }

    @Id
    @GeneratedValue
    private int providerId;

    @NotNull
    @NotBlank
    private String providerName;

    @NotNull
    @NotBlank
    private String providerAddress;

    @NotNull
    @NotBlank
    private String providerPhone;

    @NotNull
    @NotBlank
    @Email
    private String providerEmail;

    @OneToMany
    @JoinColumn(name = "providerid")
    List<ProviderServiceDTO> providerServices = new ArrayList<>();

    public int getProviderId() {
        return providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public String getProviderPhone() {
        return providerPhone;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public List<ProviderServiceDTO> getProviderServices() {
        return providerServices;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    public void setProviderPhone(String providerPhone) {
        this.providerPhone = providerPhone;
    }

    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    public static List<Provider> userSearch(String searchTerm, Iterable<Provider> providers) {
        List<Provider> providerList = new ArrayList<Provider>();
        providers.forEach(providerList::add);

        List<Provider> matchingProviders = new ArrayList<Provider>();

        for(Provider entry : providerList) {
            if(entry.getProviderName().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingProviders.add(entry);
                break;
            } else {
                for(ProviderServiceDTO service : entry.getProviderServices()) {
                    if(service.getServiceName().toLowerCase().contains(searchTerm.toLowerCase())) {
                        matchingProviders.add(entry);
                        break;
                    }
                }
            }
        }

        return matchingProviders;
    }
}
