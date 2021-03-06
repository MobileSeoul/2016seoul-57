package blackcat.hotspot.hotspot;

/**
 * Created by 현준 on 2016-10-27.
 */
public class Listviewitem {

    String name;    //이름 저장
    String nation;   //국적 저장
    String imgId;      //국기 이미지의 리소스 아이디

    public Listviewitem(String name, String nation, String imgId) {
        // TODO Auto-generated constructor stub
        //생성자함수로 전달받은 Member의 정보를 멤버변수에 저장..
        this.name= name;
        this.nation=nation;
        this.imgId=imgId;
    }

    //이 아래는 getter , setter 메소드듭입니다.
    //OOP(객체 지향 프로그래밍)은 클래스 객체 외부에서 직접 엠머변수에 접근하는 것을 지양합니다.
    //객체의 멤버변수 제어는 객체 스스로 하도록 해서 재사용성을 높인 방법이죠.
    //getter, setter 멤버 메소드들은 그 목적으로 만들어 진 것이죠.

    public void setName(String name) {
        this.name = name;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public String getNation() {
        return nation;
    }

    public String getImgId() {
        return imgId;
    }


}