package hr.fer.proinz.proggers.parkshare.security;

import hr.fer.proinz.proggers.parkshare.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public class CustomUserDetails implements UserDetails {

    private UserModel userModel;

    public CustomUserDetails(UserModel userModel){
        Objects.requireNonNull(userModel);
        this.userModel = userModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userModel.getTempPassword();
    }

    @Override
    public String getUsername() {
        return userModel.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userModel.getConfirmed();
    }

    public String getFullName() {
        return userModel.getFirstName() + " " + userModel.getSurname();
    }
}
