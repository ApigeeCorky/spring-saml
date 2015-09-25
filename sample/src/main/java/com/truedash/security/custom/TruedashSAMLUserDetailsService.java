package com.truedash.security.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

public class TruedashSAMLUserDetailsService implements SAMLUserDetailsService {

    private List<String> roles;

    /**
     * Default constructor.
     */
    public TruedashSAMLUserDetailsService() {

    }

    /**
     * Setter for roles.
     * @param r - user roles.
     */
    public void setRoles(List<String> r) {
        this.roles = r;
    }

    /**
     * Getter for roles.
     * @return list of roles.
     */
    public List<String> getRoles() {
        return this.roles;
    }

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        String email = credential.getNameID().getValue();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authorities.add(authority);

        UserDetails userDetails = new User(email, "password", true, true, true,
                true, authorities);

        return userDetails;
    }
    
}