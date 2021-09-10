package org.launchcode.ouchy.controllers;

import org.launchcode.ouchy.models.Data.ProviderRepository;
import org.launchcode.ouchy.models.Data.ProviderServiceDTORepository;
import org.launchcode.ouchy.models.Data.ServiceRepository;
import org.launchcode.ouchy.models.Provider;
import org.launchcode.ouchy.models.ProviderServiceDTO;
import org.launchcode.ouchy.models.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProviderServiceController {

    @Autowired
    private ProviderServiceDTORepository providerServiceDTORepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("AddProviderService/{providerID}")
    public String AddProviderServiceHome(Model model, @PathVariable int providerID) {
        model.addAttribute("title", "Add Provider Services");
        model.addAttribute("psDTO", new ProviderServiceDTO());
        model.addAttribute("services", serviceRepository.findAll());

        Optional optProvider = providerRepository.findById(providerID);
        Provider provider = (Provider) optProvider.get();
        model.addAttribute("provider", provider);

        return "addProviderService";
    }

    @PostMapping("AddProviderService/{providerID}")
    public String processAddProviderService(Model model, @PathVariable int providerID, @RequestParam int serviceID, @RequestParam Double serviceCost) {
        /**  Rounding service cost to two decimal places */
        serviceCost = Math.round(serviceCost*100)/100.00;

        /**  Generating redirect string to send user back to the correct /AddProviderService page */
        String redirect = "redirect:../AddProviderService/" + String.valueOf(providerID);

        /** Retrieving service from database so serviceName can be used later */
        Optional optionalService = serviceRepository.findById(serviceID);
        Service service = (Service) optionalService.get();

        /**  Generating a list of all ProviderServices currently provided */
        Iterable<ProviderServiceDTO> pServices = providerServiceDTORepository.findAllByProviderID(providerID);
        List<ProviderServiceDTO> result = new ArrayList<ProviderServiceDTO>();
        pServices.forEach(result::add);
        int dtoID = 0;

        /**  Checking to see if service already existed for provider */
        Boolean duplicateEntry = false;
        for(ProviderServiceDTO entry : result) {
            if(serviceID == entry.getServiceID()) {
                duplicateEntry = true;
                dtoID = entry.getProviderServiceDTOID();
                System.out.println("Service already listed for current provider");
            }
        }

        /**  If service exists -> update cost of service, otherwise add new ProviderServiceDTO to repository */
        if(!duplicateEntry) {
            ProviderServiceDTO psDTO = new ProviderServiceDTO(serviceID, providerID, serviceCost, service.getServiceName());
            providerServiceDTORepository.save(psDTO);
        } else {
            Optional optional = providerServiceDTORepository.findById(dtoID);
            ProviderServiceDTO currentEntry = (ProviderServiceDTO) optional.get();
            currentEntry.setServiceCost(serviceCost);
            providerServiceDTORepository.save(currentEntry);
        }

        return redirect;

    }
}
