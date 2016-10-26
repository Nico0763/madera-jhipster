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
 * A Provider.
 */
@Entity
@Table(name = "provider")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Provider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 50)
    @Column(name = "address", length = 50)
    private String address;

    @Pattern(regexp = "^[0-9]{5}$")
    @Column(name = "pc")
    private String pc;

    @Size(max = 50)
    @Column(name = "city", length = 50)
    private String city;

    @Size(max = 15)
    @Column(name = "country", length = 15)
    private String country;

    @Pattern(regexp = "^\\+[0-9]{11}$")
    @Column(name = "phone_number")
    private String phone_number;

    @Size(max = 50)
    @Pattern(regexp = "^\\w+@[a-zA-Z_]+?\\[a-zA-Z]{2,3}$")
    @Column(name = "mail", length = 50)
    private String mail;

    @OneToMany(mappedBy = "provider")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Component> components = new HashSet<>();

    @OneToMany(mappedBy = "provider")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Command> commands = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Provider name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public Provider address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPc() {
        return pc;
    }

    public Provider pc(String pc) {
        this.pc = pc;
        return this;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getCity() {
        return city;
    }

    public Provider city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public Provider country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public Provider phone_number(String phone_number) {
        this.phone_number = phone_number;
        return this;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMail() {
        return mail;
    }

    public Provider mail(String mail) {
        this.mail = mail;
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public Provider components(Set<Component> components) {
        this.components = components;
        return this;
    }

    public Provider addComponent(Component component) {
        components.add(component);
        component.setProvider(this);
        return this;
    }

    public Provider removeComponent(Component component) {
        components.remove(component);
        component.setProvider(null);
        return this;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }

    public Set<Command> getCommands() {
        return commands;
    }

    public Provider commands(Set<Command> commands) {
        this.commands = commands;
        return this;
    }

    public Provider addCommand(Command command) {
        commands.add(command);
        command.setProvider(this);
        return this;
    }

    public Provider removeCommand(Command command) {
        commands.remove(command);
        command.setProvider(null);
        return this;
    }

    public void setCommands(Set<Command> commands) {
        this.commands = commands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Provider provider = (Provider) o;
        if(provider.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, provider.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Provider{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", address='" + address + "'" +
            ", pc='" + pc + "'" +
            ", city='" + city + "'" +
            ", country='" + country + "'" +
            ", phone_number='" + phone_number + "'" +
            ", mail='" + mail + "'" +
            '}';
    }
}
