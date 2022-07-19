package com.iscas.pm.common.core.web.filter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String userId,String projectId) throws UsernameNotFoundException;
}
