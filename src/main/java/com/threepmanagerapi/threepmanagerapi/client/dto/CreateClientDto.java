package com.threepmanagerapi.threepmanagerapi.client.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientDto {
    private String name;
    private String phone;
    private String email;
    private String salesType;
//    private String registrationType;
    private String status;
    private Long regionID;
}
