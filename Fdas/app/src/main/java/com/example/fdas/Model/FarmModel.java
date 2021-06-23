package com.example.fdas.Model;

public class FarmModel  {

    String farm_area,farm_owner,farm_contact,zone_id,date_time_stamp;

    public FarmModel(String farm_area, String farm_owner, String farm_contact, String zone_id, String date_time_stamp) {
        this.farm_area = farm_area;
        this.farm_owner = farm_owner;
        this.farm_contact = farm_contact;
        this.zone_id = zone_id;
        this.date_time_stamp = date_time_stamp;
    }

    public String getFarm_area() {
        return farm_area;
    }

    public void setFarm_area(String farm_area) {
        this.farm_area = farm_area;
    }

    public String getFarm_owner() {
        return farm_owner;
    }

    public void setFarm_owner(String farm_owner) {
        this.farm_owner = farm_owner;
    }

    public String getFarm_contact() {
        return farm_contact;
    }

    public void setFarm_contact(String farm_contact) {
        this.farm_contact = farm_contact;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getDate_time_stamp() {
        return date_time_stamp;
    }

    public void setDate_time_stamp(String date_time_stamp) {
        this.date_time_stamp = date_time_stamp;
    }
}
