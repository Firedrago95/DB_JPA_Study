package hello.itemservice.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
//JPA가 사용하는 객체
@Entity
public class Item {

    //pk와 해당 필드 맵핑 , pk값을 DB에서 생성하는 IDENTITY 방식 사용
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //객체의 필드를 테이블 컬럼과 맵핑, 맵핑으로 DDL(Create) 할때 컬럼의 길이값 제공
    //스프링부트는 카멜케이스 <-> 스네이크 자동변환 사실 에너에이션 없어도 된다.
    @Column(name ="item_name", length = 10)
    private String itemName;
    private Integer price;
    private Integer quantity;

    //JPA는 public 또는 protected 기본 생성자가 필수
    public Item() {}

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
