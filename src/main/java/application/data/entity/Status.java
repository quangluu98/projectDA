package application.data.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "dbo_status")
public class Status {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "status_id")
    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status")
    private List<Order> orderList = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
