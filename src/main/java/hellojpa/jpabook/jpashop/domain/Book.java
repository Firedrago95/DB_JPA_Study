package hellojpa.jpabook.jpashop.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("B")
public class Book extends Item{

    private String author;
    private String isbn;
}
