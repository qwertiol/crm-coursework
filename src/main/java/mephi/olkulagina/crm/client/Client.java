package mephi.olkulagina.crm.client;

import jakarta.persistence.*;
import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.region.Region;
import mephi.olkulagina.crm.status.Status;
import java.time.LocalDate;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(length = 50)
    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private ClientSource source;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "loyalty_card_number")
    private String loyaltyCardNumber;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "last_activity_date")
    private LocalDate lastActivityDate;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    public Client() {
    }

    public Client(Long id, String lastName, String firstName, String middleName, String phone, String email,
                  Gender gender, ClientSource source, Region region, Company company, LocalDate birthDate,
                  String loyaltyCardNumber, LocalDate registrationDate, LocalDate lastActivityDate, Status status) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.source = source;
        this.region = region;
        this.company = company;
        this.birthDate = birthDate;
        this.loyaltyCardNumber = loyaltyCardNumber;
        this.registrationDate = registrationDate;
        this.lastActivityDate = lastActivityDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public ClientSource getSource() {
        return source;
    }

    public void setSource(ClientSource source) {
        this.source = source;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getLoyaltyCardNumber() {
        return loyaltyCardNumber;
    }

    public void setLoyaltyCardNumber(String loyaltyCardNumber) {
        this.loyaltyCardNumber = loyaltyCardNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDate lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}