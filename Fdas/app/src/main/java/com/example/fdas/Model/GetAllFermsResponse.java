package com.example.fdas.Model;

import java.util.List;

public class GetAllFermsResponse {
    List<FarmModel> farms;

    public GetAllFermsResponse(List<FarmModel> farms) {
        this.farms = farms;
    }

    public List<FarmModel> getFarms() {
        return farms;
    }

    public void setFarms(List<FarmModel> farms) {
        this.farms = farms;
    }
}
