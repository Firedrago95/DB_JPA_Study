package hellojpa.jpabook.jpashop.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
//
@DiscriminatorValue("M")
public class Movie extends Item{

    private String director;
    private String actor;
}
