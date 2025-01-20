package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "description")
public class Description implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "short_description", nullable = false)
    private String short_description;
    
    @Column(name = "warranty", length = 45, nullable = false)
    private String warranty;

    @ManyToOne
    @JoinColumn(name = "origin_country_id")
    private Origin_Country origin_Country;

    public Description() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public Origin_Country getOrigin_Country() {
        return origin_Country;
    }

    public void setOrigin_Country(Origin_Country origin_Country) {
        this.origin_Country = origin_Country;
    }

}
