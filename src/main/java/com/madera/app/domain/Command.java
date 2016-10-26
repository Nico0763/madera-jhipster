package com.madera.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.validation.constraints.Pattern;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Command.
 */
@Entity
@Table(name = "command")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[A-Z]-[A-Z]-[0-9]{1,4}$")
    @Column(name = "reference")
    private String reference;

    @Max(value = 9)
    @Column(name = "state")
    private Integer state;

    @OneToMany(mappedBy = "command")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Command_component> command_components = new HashSet<>();

    @ManyToOne
    private Provider provider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public Command reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getState() {
        return state;
    }

    public Command state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Set<Command_component> getCommand_components() {
        return command_components;
    }

    public Command command_components(Set<Command_component> command_components) {
        this.command_components = command_components;
        return this;
    }

    public Command addCommand_component(Command_component command_component) {
        command_components.add(command_component);
        command_component.setCommand(this);
        return this;
    }

    public Command removeCommand_component(Command_component command_component) {
        command_components.remove(command_component);
        command_component.setCommand(null);
        return this;
    }

    public void setCommand_components(Set<Command_component> command_components) {
        this.command_components = command_components;
    }

    public Provider getProvider() {
        return provider;
    }

    public Command provider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Command command = (Command) o;
        if(command.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, command.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Command{" +
            "id=" + id +
            ", reference='" + reference + "'" +
            ", state='" + state + "'" +
            '}';
    }
}
