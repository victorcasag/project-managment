package br.edu.infnet.victorapi.modules.auth.controller;

import br.edu.infnet.victorapi.modules.auth.dto.LoginResponseDTO;
import br.edu.infnet.victorapi.modules.users.dto.*;
import br.edu.infnet.victorapi.modules.users.entity.User;
import br.edu.infnet.victorapi.modules.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import br.edu.infnet.victorapi.config.security.TokenService;


import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "API para autenticação e autorização")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserService userService,
                                    TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Operation(
            summary = "Fazer login",
            description = "Autentica o usuário e retorna um token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "423", description = "Usuário desativado")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            Optional<User> userOpt = userService.getUserByEmail(data.email());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuário não encontrado");
            }

            User user = userOpt.get();
            if (!user.getIsActive()) {
                return ResponseEntity.status(HttpStatus.LOCKED)
                        .body("Usuário desativado. Entre em contato com o administrador.");
            }

            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            Authentication auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            userService.updateLastLogin(data.email());

            Optional<UserInfoDTO> userInfo = userService.getUserInfo(data.email());

            LoginResponseDTO response = new LoginResponseDTO(
                    token,
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().toString(),
                    userInfo.orElse(null)
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou senha incorretos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário (apenas administradores podem criar contas)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email já está em uso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDTO data) {
        try {
            if (userService.existsByEmail(data.email())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email já está em uso: " + data.email());
            }

            if (!userService.isValidEmail(data.email())) {
                return ResponseEntity.badRequest().body("Email inválido");
            }

            if (!userService.isValidPassword(data.password())) {
                return ResponseEntity.badRequest()
                        .body("Senha deve ter pelo menos 8 caracteres, incluindo maiúscula, minúscula e número");
            }

            CreateUserDTO createUserDTO = new CreateUserDTO(
                    data.name(),
                    data.email(),
                    data.password(),
                    data.phone(),
                    data.departmentId(),
                    data.position(),
                    data.role() != null ? data.role() : UserRole.ROLE_USER
            );

            User newUser = userService.createUser(createUserDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Validar token",
            description = "Verifica se o token JWT é válido e retorna informações do usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token válido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
    })
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody @Valid ValidateTokenDTO data) {
        try {
            // Validar token
            String email = tokenService.validateToken(data.token());

            if (email != null && !email.isEmpty()) {
                Optional<UserInfoDTO> userInfo = userService.getUserInfo(email);

                if (userInfo.isPresent()) {
                    return ResponseEntity.ok(userInfo.get());
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Usuário não encontrado");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token inválido ou expirado");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Refresh token",
            description = "Gera um novo token JWT baseado no token atual válido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token renovado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid ValidateTokenDTO data) {
        try {
            String email = tokenService.validateToken(data.token());

            if (email != null && !email.isEmpty()) {
                Optional<User> userOpt = userService.getUserByEmail(email);

                if (userOpt.isPresent()) {
                    User user = userOpt.get();

                    if (!user.getIsActive()) {
                        return ResponseEntity.status(HttpStatus.LOCKED)
                                .body("Usuário desativado");
                    }

                    String newToken = tokenService.generateToken(user);

                    Optional<UserInfoDTO> userInfo = userService.getUserInfo(email);

                    LoginResponseDTO response = new LoginResponseDTO(
                            newToken,
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getRole().toString(),
                            userInfo.orElse(null)
                    );

                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Usuário não encontrado");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token inválido ou expirado");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro ao renovar token: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Logout",
            description = "Invalida o token JWT (implementação opcional para blacklist)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Valid ValidateTokenDTO data) {
        try {
            // Validar token
            String email = tokenService.validateToken(data.token());

            if (email != null && !email.isEmpty()) {
                // Aqui você pode implementar uma blacklist de tokens se necessário
                // tokenService.invalidateToken(data.token());

                return ResponseEntity.ok("Logout realizado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token inválido");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro no logout: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Verificar disponibilidade de email",
            description = "Verifica se um email está disponível para uso"
    )
    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmailAvailability(@PathVariable String email) {
        try {
            boolean exists = userService.existsByEmail(email);

            CheckEmailResponseDTO response = new CheckEmailResponseDTO(
                    email,
                    !exists,
                    exists ? "Email já está em uso" : "Email disponível"
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}