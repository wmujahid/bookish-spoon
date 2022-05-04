package com.example.librarymanagement.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "customer", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Order> orderList;

    public Customer(String name) {
        this.name = name;
    }

}
