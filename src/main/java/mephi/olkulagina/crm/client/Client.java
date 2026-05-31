package mephi.olkulagina.crm.client;

import jakarta.persistence.*;
import lombok.Data;
import mephi.olkulagina.crm.status.Status;

@Entity
@Table(name = "client")
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(length = 50)
    private String phone;

    private String company;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;
}