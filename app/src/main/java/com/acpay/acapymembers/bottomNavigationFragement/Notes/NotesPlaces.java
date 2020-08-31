package com.acpay.acapymembers.bottomNavigationFragement.Notes;

import com.acpay.acapymembers.R;

import java.util.List;

public class NotesPlaces {
    private String placeId;
    private String palaceName;

    public NotesPlaces() {
    }

    public NotesPlaces(String placeId, String palaceName) {
        this.placeId = placeId;
        this.palaceName = palaceName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPalaceName() {
        return palaceName;
    }

    public void setPalaceName(String palaceName) {
        this.palaceName = palaceName;
    }

}
