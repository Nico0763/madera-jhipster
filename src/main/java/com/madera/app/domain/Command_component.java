package com.madera.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Command_component.
 */
@Entity
@Table(name = "command_component")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Command_component implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Max(value = 999)
    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    private Component component;

    @ManyToOne
    private Command command;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Command_component quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Component getComponent() {
        return component;
    }

    public Command_component component(Component component) {
        this.component = component;
        return this;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Command getCommand() {
        return command;
    }

    public Command_component command(Command command) {
        this.command = command;
        return this;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Command_component command_component = (Command_component) o;
        if(command_component.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, command_component.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Command_component{" +
            "id=" + id +
            ", quantity='" + quantity + "'" +
            '}';
    }
}
