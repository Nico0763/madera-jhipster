package com.madera.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Assortment.
 */
@Entity
@Table(name = "assortment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Assortment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Lob
    @Column(name = "skeleton_conception_mode")
    private byte[] skeleton_conception_mode;

    @Column(name = "skeleton_conception_mode_content_type")
    private String skeleton_conception_modeContentType;

    @ManyToOne
    private Finition_ext finition_ext;

    @ManyToOne
    private Insulating_type insulating_type;

    @ManyToOne
    private Frame frame;

    @ManyToOne
    private Cover_type cover_type;

    @OneToMany(mappedBy = "assortment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Module> modules = new HashSet<>();

    @OneToMany(mappedBy = "assortment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pattern> patterns = new HashSet<>();

    @OneToMany(mappedBy = "assortment")
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

    public Assortment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getSkeleton_conception_mode() {
        return skeleton_conception_mode;
    }

    public Assortment skeleton_conception_mode(byte[] skeleton_conception_mode) {
        this.skeleton_conception_mode = skeleton_conception_mode;
        return this;
    }

    public void setSkeleton_conception_mode(byte[] skeleton_conception_mode) {
        this.skeleton_conception_mode = skeleton_conception_mode;
    }

    public String getSkeleton_conception_modeContentType() {
        return skeleton_conception_modeContentType;
    }

    public Assortment skeleton_conception_modeContentType(String skeleton_conception_modeContentType) {
        this.skeleton_conception_modeContentType = skeleton_conception_modeContentType;
        return this;
    }

    public void setSkeleton_conception_modeContentType(String skeleton_conception_modeContentType) {
        this.skeleton_conception_modeContentType = skeleton_conception_modeContentType;
    }

    public Finition_ext getFinition_ext() {
        return finition_ext;
    }

    public Assortment finition_ext(Finition_ext finition_ext) {
        this.finition_ext = finition_ext;
        return this;
    }

    public void setFinition_ext(Finition_ext finition_ext) {
        this.finition_ext = finition_ext;
    }

    public Insulating_type getInsulating_type() {
        return insulating_type;
    }

    public Assortment insulating_type(Insulating_type insulating_type) {
        this.insulating_type = insulating_type;
        return this;
    }

    public void setInsulating_type(Insulating_type insulating_type) {
        this.insulating_type = insulating_type;
    }

    public Frame getFrame() {
        return frame;
    }

    public Assortment frame(Frame frame) {
        this.frame = frame;
        return this;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public Cover_type getCover_type() {
        return cover_type;
    }

    public Assortment cover_type(Cover_type cover_type) {
        this.cover_type = cover_type;
        return this;
    }

    public void setCover_type(Cover_type cover_type) {
        this.cover_type = cover_type;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Assortment modules(Set<Module> modules) {
        this.modules = modules;
        return this;
    }

    public Assortment addModule(Module module) {
        modules.add(module);
        module.setAssortment(this);
        return this;
    }

    public Assortment removeModule(Module module) {
        modules.remove(module);
        module.setAssortment(null);
        return this;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    public Set<Pattern> getPatterns() {
        return patterns;
    }

    public Assortment patterns(Set<Pattern> patterns) {
        this.patterns = patterns;
        return this;
    }

    public Assortment addPattern(Pattern pattern) {
        patterns.add(pattern);
        pattern.setAssortment(this);
        return this;
    }

    public Assortment removePattern(Pattern pattern) {
        patterns.remove(pattern);
        pattern.setAssortment(null);
        return this;
    }

    public void setPatterns(Set<Pattern> patterns) {
        this.patterns = patterns;
    }

    public Set<Quotation> getQuotations() {
        return quotations;
    }

    public Assortment quotations(Set<Quotation> quotations) {
        this.quotations = quotations;
        return this;
    }

    public Assortment addQuotation(Quotation quotation) {
        quotations.add(quotation);
        quotation.setAssortment(this);
        return this;
    }

    public Assortment removeQuotation(Quotation quotation) {
        quotations.remove(quotation);
        quotation.setAssortment(null);
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
        Assortment assortment = (Assortment) o;
        if(assortment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, assortment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Assortment{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", skeleton_conception_mode='" + skeleton_conception_mode + "'" +
            ", skeleton_conception_modeContentType='" + skeleton_conception_modeContentType + "'" +
            '}';
    }
}
