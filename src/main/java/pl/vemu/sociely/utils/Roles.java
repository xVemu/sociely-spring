package pl.vemu.sociely.utils;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    ADMIN, MODERATOR, USER;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
