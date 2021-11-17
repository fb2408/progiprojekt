package hr.fer.proinz.proggers.parkshare.security;

import hr.fer.proinz.proggers.parkshare.model.UserModel;
import hr.fer.proinz.proggers.parkshare.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepo;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository){
        userRepo = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserModel user = userRepo.findByEmail(s);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(user.isAdmin()){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else if(user.isOwner()){
            authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));
        }else{
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }
        boolean disabled = !user.getConfirmed();
        return User.withUsername(user.getEmail())
                .password(user.getTempPassword())
                .disabled(disabled)
                .authorities((authorities))
                .build();
    }
}
