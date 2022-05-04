package com.example.librarymanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_out_date", length = 50, nullable = false)
    private String checkOutDate;

    @Column(name = "date_due", length = 50, nullable = false)
    private String dateDue;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Book> bookList;

    public Order(String checkOutDate, String dateDue) {
        this.checkOutDate = checkOutDate;
        this.dateDue = dateDue;
    }

}