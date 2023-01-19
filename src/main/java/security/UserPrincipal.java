package security;

import entities.Member;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPrincipal implements Principal {

  private String membername;
  private List<String> roles = new ArrayList<>();

  /* Create a UserPrincipal, given the Entity class Member*/
  public UserPrincipal(Member member) {
    this.membername = member.getMemberName();
    this.roles = member.getRolesAsStrings();
  }

  public UserPrincipal(String membername, String[] roles) {
    super();
    this.membername = membername;
    this.roles = Arrays.asList(roles);
  }

  @Override
  public String getName() {
    return membername;
  }

  public boolean isUserInRole(String role) {
    return this.roles.contains(role);
  }
}