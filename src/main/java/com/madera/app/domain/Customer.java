package com.madera.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import javax.validation.constraints.Pattern;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 25)
    @Column(name = "firstname", length = 25)
    private String firstname;

    @Size(max = 50)
    @Column(name = "address", length = 50)
    private String address;

    @Size(max = 5)
    @Column(name = "pc", length = 5)
    private String pc;

    @Size(max = 50)
    @Column(name = "city", length = 50)
    private String city;

    @Pattern(regexp = "^[0-9]{10}$")
    @Column(name = "phone_number")
    private String phone_number;

    @Pattern(regexp = "^(([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+([;.](([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+)*$")
    @Column(name = "mail")
    private String mail;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Quotation> quotations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Customer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public Customer firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getAddress() {
        return address;
    }

    public Customer address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPc() {
        return pc;
    }

    public Customer pc(String pc) {
        this.pc = pc;
        return this;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getCity() {
        return city;
    }

    public Customer city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public Customer phone_number(String phone_number) {
        this.phone_number = phone_number;
        return this;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMail() {
        return mail;
    }

    public Customer mail(String mail) {
        this.mail = mail;
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Set<Quotation> getQuotations() {
        return quotations;
    }

    public Customer quotations(Set<Quotation> quotations) {
        this.quotations = quotations;
        return this;
    }

    public Customer addQuotation(Quotation quotation) {
        quotations.add(quotation);
        quotation.setCustomer(this);
        return this;
    }

    public Customer removeQuotation(Quotation quotation) {
        quotations.remove(quotation);
        quotation.setCustomer(null);
        return this;
    }

    public void setQuotations(Set<Quotation> quotations) {
        this.quotations = quotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if(customer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", firstname='" + firstname + "'" +
            ", address='" + address + "'" +
            ", pc='" + pc + "'" +
            ", city='" + city + "'" +
            ", phone_number='" + phone_number + "'" +
            ", mail='" + mail + "'" +
            '}';
    }
}
