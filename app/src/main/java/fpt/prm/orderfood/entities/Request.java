package fpt.prm.orderfood.entities;

import java.util.List;

public class Request {
    private String phone, name, address, total;
    private List<Cart> foodOrder;

    public Request(String phone, String name, String address, String total, List<Cart> foodOrder) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.foodOrder = foodOrder;
    }

    public Request() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Cart> getFoodOrder() {
        return foodOrder;
    }

    public void setFoodOrder(List<Cart> foodOrder) {
        this.foodOrder = foodOrder;
    }
}
