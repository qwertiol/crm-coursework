package mephi.olkulagina.crm.client;

import jakarta.persistence.*;
import mephi.olkulagina.crm.company.Company;
import mephi.olkulagina.crm.region.Region;
import mephi.olkulagina.crm.status.Status;
import java.time.LocalDate;

@Entity
@Table(name = "client")
public class Client {

    private static final int PHONE_LENGTH = 50;
    private static final int SPECIAL_CONDITIONS_LENGTH = 500;
    private static final int CLIENT_LEVEL_LENGTH = 20;
    private static final int SOURCE_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(length = PHONE_LENGTH)
    private String phone;

    private String email;

    private String position;

    private String department;

    @Enumerated(EnumType.STRING)
    private ClientLevel clientLevel;

    @Column(length = SPECIAL_CONDITIONS_LENGTH)
    private String specialConditions;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "last_activity_date")
    private LocalDate lastActivityDate;

    @Enumerated(EnumType.STRING)
    private ClientSource source;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    public Client() {
    }

    public Client(Long id, String lastName, String firstName, String middleName, String phone,
                  String email, String position, String department, ClientLevel clientLevel,
                  String specialConditions, LocalDate registrationDate, LocalDate lastActivityDate,
                  ClientSource source, Region region, Company company, Status status) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.phone = phone;
        this.email = email;
        this.position = position;
        this.department = department;
        this.clientLevel = clientLevel;
        this.specialConditions = specialConditions;
        this.registrationDate = registrationDate;
        this.lastActivityDate = lastActivityDate;
        this.source = source;
        this.region = region;
        this.company = company;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public ClientLevel getClientLevel() { return clientLevel; }
    public void setClientLevel(ClientLevel clientLevel) { this.clientLevel = clientLevel; }

    public String getSpecialConditions() { return specialConditions; }
    public void setSpecialConditions(String specialConditions) { this.specialConditions = specialConditions; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public LocalDate getLastActivityDate() { return lastActivityDate; }
    public void setLastActivityDate(LocalDate lastActivityDate) { this.lastActivityDate = lastActivityDate; }

    public ClientSource getSource() { return source; }
    public void setSource(ClientSource source) { this.source = source; }

    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}