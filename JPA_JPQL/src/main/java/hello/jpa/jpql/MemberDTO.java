package hello.jpa.jpql;


public class MemberDTO {
    private String usernaeme;
    private int age;

    public MemberDTO(String usernaeme, int age) {
        this.usernaeme = usernaeme;
        this.age = age;
    }

    public String getUsernaeme() {
        return usernaeme;
    }

    public void setUsernaeme(String usernaeme) {
        this.usernaeme = usernaeme;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
