package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "member")
public class Member implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "member_name", length = 25)
  private String memberName;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "member_pass")
  private String memberPass;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "phone", nullable = false)
  private String phone;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "birth_year", nullable = false)
  private Integer birthYear;

  @Column(name = "account", nullable = false)
  private BigDecimal account;

  @ManyToMany
  @JoinTable(name = "member_roles", joinColumns = {
    @JoinColumn(name = "member_name", referencedColumnName = "member_name")}, inverseJoinColumns = {
    @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
  private List<Role> roleList = new ArrayList<>();

  @ManyToMany
  @JoinTable(name = "member_assignment", joinColumns = {
          @JoinColumn(name = "member_name", referencedColumnName = "member_name")}, inverseJoinColumns = {
          @JoinColumn(name = "assigment_id", referencedColumnName = "id")})
  private Set<Assignment> assignments = new LinkedHashSet<>();

  public Member() {}

  public Member(String memberName, String memberPass, String address, String phone, String email, Integer birthYear) {
    this.memberName = memberName;
    this.memberPass = BCrypt.hashpw(memberPass, BCrypt.gensalt());
    this.address = address;
    this.phone = phone;
    this.email = email;
    this.birthYear = birthYear;
    this.account = BigDecimal.valueOf(0);
  }

  public List<String> getRolesAsStrings() {
    if (roleList.isEmpty()) {
      return null;
    }
    List<String> rolesAsStrings = new ArrayList<>();
    roleList.forEach((role) -> {
      rolesAsStrings.add(role.getRoleName());
    });
    return rolesAsStrings;
  }

  public boolean verifyPassword(String pw) {
    return BCrypt.checkpw(pw, this.memberPass);
  }

  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  public String getMemberPass() {
    return this.memberPass;
  }

  public void setMemberPass(String memberPass) {
    this.memberPass = memberPass;
  }

  public List<Role> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<Role> roleList) {
    this.roleList = roleList;
  }

  public void addRole(Role userRole) {
    roleList.add(userRole);
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getBirthYear() {
    return birthYear;
  }

  public void setBirthYear(Integer birthYear) {
    this.birthYear = birthYear;
  }

  public BigDecimal getAccount() {
    return account;
  }

  public void setAccount(BigDecimal account) {
    this.account = account;
  }

  public Set<Assignment> getAssignments() {
    return assignments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Member member = (Member) o;
    return memberName.equals(member.memberName) && memberPass.equals(member.memberPass)
            && address.equals(member.address) && phone.equals(member.phone) && email.equals(member.email)
            && birthYear.equals(member.birthYear) && account.equals(member.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberName, memberPass, address, phone, email, birthYear, account);
  }
}
