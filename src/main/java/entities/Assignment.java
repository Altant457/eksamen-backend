package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
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
    private Set<Member> members;

    @ManyToOne
    @JoinColumn(name = "dinner_event_id", nullable = false)
    private DinnerEvent dinnerEvent;

    public Assignment() {}

    public Assignment(String familyName, String contactInfo, DinnerEvent dinnerEvent) {
        this.familyName = familyName;
        this.createDate = java.sql.Date.valueOf(LocalDate.now());
        this.contactInfo = contactInfo;
        this.dinnerEvent = dinnerEvent;
        dinnerEvent.getAssignments().add(this);
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

    public Set<Member> getMembers() {
        return members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return id.equals(that.id) && familyName.equals(that.familyName) && createDate.equals(that.createDate)
                && contactInfo.equals(that.contactInfo) && members.equals(that.members)
                && dinnerEvent.equals(that.dinnerEvent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, familyName, createDate, contactInfo, members, dinnerEvent);
    }
}