package co.com.japl.ea.dto.system;

import org.pyt.common.abstracts.ADto;

public class MenuPermUsersDTO extends ADto {
	private static final long serialVersionUID = 1L;
	private MenuDTO menu;
	private UsuarioDTO user;
	private GroupUsersDTO groupUsers;
	private PermissionDTO perm;
	private Integer state;

	public MenuDTO getMenu() {
		return this.menu;
	}

	public void setMenu(MenuDTO menu) {
		this.menu = menu;
	}

	public UsuarioDTO getUser() {
		return this.user;
	}

	public void setUser(UsuarioDTO user) {
		this.user = user;
	}

	public PermissionDTO getPerm() {
		return this.perm;
	}

	public void setPerm(PermissionDTO perm) {
		this.perm = perm;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public GroupUsersDTO getGroupUsers() {
		return this.groupUsers;
	}

	public void setGroupUsers(GroupUsersDTO groupUsers) {
		this.groupUsers = groupUsers;
	}
}
