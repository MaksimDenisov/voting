package ru.denisovmaksim.voting.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Table(name = "restaurants")
@SQLDelete(sql = "UPDATE restaurants SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonIgnore
    private List<Dish> dishes;

    private boolean deleted = Boolean.FALSE;

    public Restaurant(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
