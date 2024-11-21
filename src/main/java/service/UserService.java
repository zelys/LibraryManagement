package service;

import entity.User;
import repository.UserRepository;

import java.util.List;
import java.util.regex.Pattern;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String saveUser(User user) {
        if (!isDniValid(user.getDni())) {
            return "Formato de rut inválido. Debe ser '12345678-9' o '12345678-K'";
        }
        if (dniIsExists(user.getDni())) {
            return "El rut ingresado ya está registrado.";
        }
        if (emailIsExists(user.getEmail())) {
            return "El correo electrónico ya está registrado.";
        }
        userRepository.save(user);
        return "Usuario guardado exitosamente.";
    }

    public void getAll() {
        List<User> users = userRepository.findAll();
        System.out.println("Lista de usuarios:");
        for (User user : users) {
            System.out.println("Nombre: " + user.getUsername() + " | Rol: " + user.getRole().getName());
        }
    }

    private boolean isDniValid(String password) {
        String regex = "^\\d{7,8}-[0-9Kk]$";
        return password != null && Pattern.matches(regex, password);
    }

    private boolean dniIsExists(String dni) {
        return userRepository.existsByDni(dni);
    }

    private boolean emailIsExists(String email) {
        return userRepository.existsByEmail(email);
    }
}

