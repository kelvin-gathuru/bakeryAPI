package com.threepmanagerapi.threepmanagerapi.client.controller;

import com.threepmanagerapi.threepmanagerapi.client.dto.CreateClientDto;
import com.threepmanagerapi.threepmanagerapi.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sales")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/client/create")
    public ResponseEntity createClient(@RequestHeader ("Authorization") String token,
            @RequestBody CreateClientDto createClientDto) {
        return clientService.createClient(token, createClientDto);
    }
    @GetMapping("/client/get")
    public ResponseEntity getClients(@RequestHeader ("Authorization") String token) {
        return clientService.getClients();
    }
}
