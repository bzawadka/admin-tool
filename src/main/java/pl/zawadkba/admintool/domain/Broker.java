package pl.zawadkba.admintool.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Broker.
 */
@Entity
@Table(name = "broker")
public class Broker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "broker_status_id")
    private BrokerStatus brokerStatus;

    @ManyToOne
    @JoinColumn(name = "message_version_id")
    private MessageVersion messageVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BrokerStatus getBrokerStatus() {
        return brokerStatus;
    }

    public void setBrokerStatus(BrokerStatus brokerStatus) {
        this.brokerStatus = brokerStatus;
    }

    public MessageVersion getMessageVersion() {
        return messageVersion;
    }

    public void setMessageVersion(MessageVersion messageVersion) {
        this.messageVersion = messageVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Broker broker = (Broker) o;
        return Objects.equals(id, broker.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Broker{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", comment='" + comment + "'" +
            '}';
    }
}
