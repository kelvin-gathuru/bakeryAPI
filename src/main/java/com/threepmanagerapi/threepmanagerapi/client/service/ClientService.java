package com.threepmanagerapi.threepmanagerapi.client.service;
import com.threepmanagerapi.threepmanagerapi.client.dto.CreateClientDto;
import com.threepmanagerapi.threepmanagerapi.client.model.ArchivedClient;
import com.threepmanagerapi.threepmanagerapi.client.model.Client;
import com.threepmanagerapi.threepmanagerapi.client.model.RegistrationType;
import com.threepmanagerapi.threepmanagerapi.client.model.SalesType;
import com.threepmanagerapi.threepmanagerapi.client.repository.ArchivedClientRepository;
import com.threepmanagerapi.threepmanagerapi.client.repository.ClientRepository;
import com.threepmanagerapi.threepmanagerapi.region.model.Region;
import com.threepmanagerapi.threepmanagerapi.region.repository.RegionRepository;
import com.threepmanagerapi.threepmanagerapi.settings.Model.Status;
import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
import com.threepmanagerapi.threepmanagerapi.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClientService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ArchivedClientRepository archivedClientRepository;
    public ResponseEntity createClient(String token, CreateClientDto createClientDto){
        try{
            Optional<Client> existingClient = clientRepository.findByPhone(createClientDto.getPhone());
            Long userID = jwtService.extractuserID(token);
            if(existingClient.isPresent()){
                return responseService.formulateResponse(
                        null,
                        "Phone Number Already Exists",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }
            Client client = new Client();

            Region region = regionRepository.findByRegionID(createClientDto.getRegionID());

            if(region.getStatus()==Status.INACTIVE){
                return responseService.formulateResponse(
                        null,
                        "Region is Inactive",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }

            client.setName(createClientDto.getName());
            client.setPhone(createClientDto.getPhone());
            client.setUser(userRepository.findByUserID(userID));
            client.setRegion(region);
            client.setRegistrationType(RegistrationType.valueOf(createClientDto.getRegistrationType()));
            client.setSalesType(SalesType.valueOf(createClientDto.getSalesType()));
            client.setStatus(Status.valueOf(createClientDto.getStatus()));

            clientRepository.save(client);
            return responseService.formulateResponse(
                    null,
                    "Client added successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception creating client ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

    public ResponseEntity getClients(){
        try{
            List<Client> clients = clientRepository.findAll();
            return responseService.formulateResponse(
                    clients,
                    "Client fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching clients ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

    public ResponseEntity archiveClient(Client client){
        try{
            Optional<Client> existingClient = clientRepository.findByPhone(client.getPhone());
            if(existingClient.isEmpty()){
                return responseService.formulateResponse(
                        null,
                        "Client does not exist",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }
            ArchivedClient archivedClient = new ArchivedClient();

            archivedClient.setName(client.getName());
            archivedClient.setPhone(client.getPhone());
            archivedClient.setUser(client.getUser());
            archivedClient.setRegion(client.getRegion());
            archivedClient.setRegistrationType(client.getRegistrationType());
            archivedClient.setSalesType(client.getSalesType());
            archivedClient.setStatus(client.getStatus());

            archivedClientRepository.save(archivedClient);
            clientRepository.delete(client);
            return responseService.formulateResponse(
                    null,
                    "Client Archived successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception archiving client ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

    public ResponseEntity updateClient(Client client){
        try{
            Client existingClient = clientRepository.findByClientID(client.getClientID());
            System.out.println(client);
            System.out.println(existingClient);
            if(existingClient==null){
                return responseService.formulateResponse(
                        null,
                        "Client does not Exist",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }
            if(client.getRegion().getStatus()==Status.INACTIVE){
                return responseService.formulateResponse(
                        null,
                        "Selected Region is Inactive",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }

            clientRepository.save(client);
            return responseService.formulateResponse(
                    null,
                    "Client updated successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception updating client ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
}
