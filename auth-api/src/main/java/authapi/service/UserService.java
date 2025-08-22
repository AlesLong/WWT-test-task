package authapi.service;

import authapi.entity.User;
import authapi.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) { this.repo = repo; }

    public void register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(password));
        repo.save(user);
    }

    public User validate(String email, String password) {
        User user = repo.findByEmail(email).orElseThrow();
        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

}
