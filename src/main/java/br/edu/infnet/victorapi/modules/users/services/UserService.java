package br.edu.infnet.victorapi.modules.users.services;

import br.edu.infnet.victorapi.modules.users.entity.User;
import br.edu.infnet.victorapi.modules.users.repository.IUserRepository;
import br.edu.infnet.victorapi.modules.users.repository.UserRepository;
import br.edu.infnet.victorapi.modules.users.dto.UserInfoDTO;
import br.edu.infnet.victorapi.modules.users.dto.CreateUserDTO;
import br.edu.infnet.victorapi.modules.users.dto.UpdateUserDTO;
import br.edu.infnet.victorapi.modules.users.dto.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final IUserRepository userRepository;
    private final UserRepository userRepositoryImpl;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository userRepository,
                       UserRepository userRepositoryImpl,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRepositoryImpl = userRepositoryImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmailForAuth(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }
        return user;
    }

    public User authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
            User user = userOpt.get();
            updateLastLogin(email);
            return user;
        }
        return null;
    }

    public User createUser(CreateUserDTO createUserDTO) {
        if (userRepository.existsByEmail(createUserDTO.email())) {
            throw new IllegalArgumentException("Email já está em uso: " + createUserDTO.email());
        }

        User user = new User();
        user.setName(createUserDTO.name());
        user.setEmail(createUserDTO.email());
        user.setPasswordHash(passwordEncoder.encode(createUserDTO.password()));
        user.setPhone(createUserDTO.phone());
        user.setDepartmentId(createUserDTO.departmentId());
        user.setPosition(createUserDTO.position());
        user.setRole(createUserDTO.role() != null ? createUserDTO.role() : UserRole.ROLE_USER);

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal.id")
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findByIsActiveTrue(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal.id")
    public User updateUser(Integer userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + userId));

        if (updateUserDTO.name() != null) {
            user.setName(updateUserDTO.name());
        }
        if (updateUserDTO.phone() != null) {
            user.setPhone(updateUserDTO.phone());
        }
        if (updateUserDTO.position() != null) {
            user.setPosition(updateUserDTO.position());
        }
        if (updateUserDTO.departmentId() != null) {
            user.setDepartmentId(updateUserDTO.departmentId());
        }

        if (updateUserDTO.role() != null) {
            user.setRole(updateUserDTO.role());
        }

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + userId));

        userRepositoryImpl.deactivateUser(userId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public List<User> getUsersByDepartment(Integer departmentId) {
        return userRepository.findByDepartmentId(departmentId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public Page<User> getUsersWithFilters(String name, Integer departmentId,
                                          UserRole role, Boolean isActive,
                                          Pageable pageable) {
        return userRepositoryImpl.findUsersWithFilters(name, departmentId, role, isActive, pageable);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN') or #email == authentication.name")
    public Optional<UserInfoDTO> getUserInfo(String email) {
        return userRepository.findUserInfoByEmail(email);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #email == authentication.name")
    public boolean updatePassword(String email, String currentPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
                return false;
            }

            String encodedPassword = passwordEncoder.encode(newPassword);
            return userRepositoryImpl.updatePassword(email, encodedPassword);
        }
        return false;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean resetPassword(String email, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        return userRepositoryImpl.updatePassword(email, encodedPassword);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean activateUser(Integer userId) {
        return userRepositoryImpl.activateUser(userId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean deactivateUser(Integer userId) {
        return userRepositoryImpl.deactivateUser(userId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateLastLogin(String email) {
        userRepository.updateLastLogin(email, LocalDateTime.now());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long countUsersByDepartment(Integer departmentId) {
        return userRepository.countActiveUsersByDepartment(departmentId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getInactiveUsers(int daysInactive) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysInactive);
        return userRepositoryImpl.findInactiveUsers(cutoffDate);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getRecentUsers(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        LocalDateTime endDate = LocalDateTime.now();
        return userRepository.findUsersCreatedBetween(startDate, endDate);
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean isValidPassword(String password) {
        // Pelo menos 8 caracteres, 1 maiúscula, 1 minúscula, 1 número
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*");
    }
}