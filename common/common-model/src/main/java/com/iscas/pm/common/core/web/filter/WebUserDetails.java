package com.iscas.pm.common.core.web.filter;

import com.iscas.pm.common.core.model.Permission;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 * @author ： zzc
 * 类实现 UserDetails 接口，该类在验证成功后会被保存在当前回话的principal对象中.
 * 获得对象的方式：
 * WebUserDetails webUserDetails = (WebUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 * 如果需要包括用户的其他属性，可以在该类中增加相应属性即可
 */

@Data
public class WebUserDetails implements UserDetails {

	private static final long serialVersionUID = -8242940190960961504L;

	private String userId;
	private List<Permission> sysPermissions;
	private boolean userEnabled;
	private Collection<GrantedAuthority> authorities;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;

	WebUserDetails(
			String userId,
			List<Permission> sysPermissions,
			boolean userEnabled,
			Collection<GrantedAuthority> authorities
	)
	{
		this.userId =userId;
		this.sysPermissions= sysPermissions;
		this.userEnabled = userEnabled;
		this.authorities = authorities;
		//这里先初始都为true，如果需要深度控制，可完善
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}


	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {		
		return this.userEnabled;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String getUsername() {
		return this.userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public List<Permission> getSysPermissions() {
		return sysPermissions;
	}

//	public void setSysPermissions(List<Permission> sysPermissions) {
//		this.sysPermissions = sysPermissions;
//	}

}
