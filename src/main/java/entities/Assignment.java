package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "assignment")
public class Assignment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "family_name", nullable = false)
    private String familyName;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @ManyToMany(mappedBy = "assignments")
    private Set<Member> members = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "dinner_event_id", nullable = false)
    private DinnerEvent dinnerEvent;

    public Assignment() {}

    public Assignment(Member member, String familyName, String contactInfo) {
        this.familyName = familyName;
        this.createDate = java.sql.Date.valueOf(LocalDate.now());
        this.contactInfo = contactInfo;
        member.getAssignments().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public DinnerEvent getDinnerEvent() {
        return dinnerEvent;
    }

    public void setDinnerEvent(DinnerEvent dinnerEvent) {
        this.dinnerEvent = dinnerEvent;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public void addMember(Member member) {
        this.members.add(member);
        member.getAssignments().add(this);
    }

    public void removeMember(Member member) {
        this.members.remove(member);
        member.getAssignments().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return id.equals(that.id) && familyName.equals(that.familyName) && createDate.equals(that.createDate)
                && contactInfo.equals(that.contactInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, familyName, createDate, contactInfo);
    }
}