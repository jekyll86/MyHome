package com.jeckyll86.xenont.myhome.datamodel;

/**
 * Created by XenonT on 24/04/2015.
 */
public class Home {
    private Long id;
    private String name;
    private String address;
    private Integer port;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    public String toString(){
        StringBuilder builder = new StringBuilder();
        return  builder.append("ID: ").append(id)
                .append("\nName: ").append(name)
                .append("\nAddress: ").append(address)
                .append("\nPort: ").append(port)
                .toString();
    }
}
