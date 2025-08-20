package br.edu.infnet.victorapi.modules.auth.dto;

import br.edu.infnet.victorapi.modules.users.dto.UserInfoDTO;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "token",
        "userId",
        "name",
        "email",
        "role",
        "userInfo"
})
public record LoginResponseDTO(
        String token,
        Integer userId,
        String name,
        String email,
        String role,
        UserInfoDTO userInfo
) {}