package br.edu.infnet.victorapi.modules.users.controller;

import br.edu.infnet.victorapi.modules.users.entity.User;
import br.edu.infnet.victorapi.modules.users.services.UserService;
import br.edu.infnet.victorapi.modules.users.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "br/edu/infnet/victorapi/modules/users", description = "API para gerenciamento de usuários")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Criar novo usuário",
            description = "Cria um novo usuário no sistema. Apenas administradores podem criar usuários."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "409", description = "Email já está em uso")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        try {
            if (!userService.isValidEmail(createUserDTO.email())) {
                return ResponseEntity.badRequest().body("Email inválido");
            }

            if (!userService.isValidPassword(createUserDTO.password())) {
                return ResponseEntity.badRequest()
                        .body("Senha deve ter pelo menos 8 caracteres, incluindo maiúscula, minúscula e número");
            }

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
            summary = "Buscar usuário por ID",
            description = "Retorna um usuário específico pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable Integer id) {
        try {
            Optional<User> user = userService.getUserById(id);
            return user.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar usuário por email",
            description = "Retorna um usuário específico pelo email"
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(
            @Parameter(description = "Email do usuário", required = true)
            @PathVariable String email) {
        try {
            Optional<User> user = userService.getUserByEmail(email);
            return user.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna uma lista paginada de todos os usuários ativos"
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getAllUsers(
            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Campo para ordenação", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,

            @Parameter(description = "Direção da ordenação", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() :
                    Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> users = userService.getAllUsers(pageable);

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza informações de um usuário existente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            User updatedUser = userService.updateUser(id, updateUserDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Desativar usuário",
            description = "Desativa um usuário (soft delete). Apenas administradores."
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar usuários por nome",
            description = "Busca usuários cujo nome contenha o texto fornecido"
    )
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> searchUsers(
            @Parameter(description = "Nome para busca", required = true)
            @RequestParam String name) {
        try {
            List<User> users = userService.searchUsersByName(name);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar usuários por departamento",
            description = "Retorna todos os usuários de um departamento específico"
    )
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getUsersByDepartment(@PathVariable Integer departmentId) {
        try {
            List<User> users = userService.getUsersByDepartment(departmentId);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar usuários com filtros avançados",
            description = "Busca usuários com múltiplos filtros opcionais"
    )
    @GetMapping("/filter")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getUsersWithFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
            Page<User> users = userService.getUsersWithFilters(name, departmentId, role, isActive, pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obter informações completas do usuário",
            description = "Retorna informações detalhadas do usuário incluindo departamento"
    )
    @GetMapping("/info/{email}")
    public ResponseEntity<?> getUserInfo(@PathVariable String email) {
        try {
            Optional<UserInfoDTO> userInfo = userService.getUserInfo(email);
            return userInfo.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Alterar senha do usuário",
            description = "Permite que o usuário altere sua própria senha"
    )
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            if (!userService.isValidPassword(changePasswordDTO.newPassword())) {
                return ResponseEntity.badRequest()
                        .body("Nova senha deve ter pelo menos 8 caracteres, incluindo maiúscula, minúscula e número");
            }

            boolean success = userService.updatePassword(
                    changePasswordDTO.email(),
                    changePasswordDTO.currentPassword(),
                    changePasswordDTO.newPassword()
            );

            if (success) {
                return ResponseEntity.ok("Senha alterada com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Senha atual incorreta");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Resetar senha do usuário",
            description = "Permite que administradores resetem a senha de qualquer usuário"
    )
    @PutMapping("/reset-password")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            if (!userService.isValidPassword(resetPasswordDTO.newPassword())) {
                return ResponseEntity.badRequest()
                        .body("Nova senha deve ter pelo menos 8 caracteres, incluindo maiúscula, minúscula e número");
            }

            boolean success = userService.resetPassword(resetPasswordDTO.email(), resetPasswordDTO.newPassword());

            if (success) {
                return ResponseEntity.ok("Senha resetada com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Ativar usuário",
            description = "Ativa um usuário desativado"
    )
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> activateUser(@PathVariable Integer id) {
        try {
            boolean success = userService.activateUser(id);
            if (success) {
                return ResponseEntity.ok("Usuário ativado com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Desativar usuário",
            description = "Desativa um usuário ativo"
    )
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deactivateUser(@PathVariable Integer id) {
        try {
            boolean success = userService.deactivateUser(id);
            if (success) {
                return ResponseEntity.ok("Usuário desativado com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Contar usuários por departamento",
            description = "Retorna a quantidade de usuários ativos em um departamento"
    )
    @GetMapping("/stats/department/{departmentId}/count")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> countUsersByDepartment(@PathVariable Integer departmentId) {
        try {
            Long count = userService.countUsersByDepartment(departmentId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obter usuários inativos",
            description = "Retorna usuários que não fizeram login há X dias"
    )
    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getInactiveUsers(
            @Parameter(description = "Número de dias de inatividade", example = "30")
            @RequestParam(defaultValue = "30") int days) {
        try {
            List<User> inactiveUsers = userService.getInactiveUsers(days);
            return ResponseEntity.ok(inactiveUsers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obter usuários recentes",
            description = "Retorna usuários criados nos últimos X dias"
    )
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getRecentUsers(
            @Parameter(description = "Número de dias", example = "7")
            @RequestParam(defaultValue = "7") int days) {
        try {
            List<User> recentUsers = userService.getRecentUsers(days);
            return ResponseEntity.ok(recentUsers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}