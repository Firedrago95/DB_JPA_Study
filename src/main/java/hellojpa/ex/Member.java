package hellojpa.ex;

import jakarta.persistence.*;
import java.util.Date;


public class Member {

    @Id @GeneratedValue(strategy  = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    private String username;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Lob
    private String description;


    public Member() {}

}
