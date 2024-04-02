package com.acpay.acapymembers.Order;

import android.view.View;

<<<<<<< HEAD

public class Order {
    private String orderNum;
=======
import com.acpay.acapymembers.Order.progress.boxes;
import com.acpay.acapymembers.Order.progress.boxesAdapter;

import java.util.ArrayList;
import java.util.List;


public class Order {
    private String orderNum;
    private String userName;
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a
    private String Date;
    private String Time;
    private String Place;
    private String location;
    private String fixType;
    private String classMatter;
    private String DliverCost;
    private String notes;
    private String file;

<<<<<<< HEAD


    public Order(String orderNum, String Date, String Time, String Place, String location, String fixType, String classMatter, String DliverCost, String notes, String file) {
=======
    private List<boxes> ProgressList;

    private boxesAdapter progListView;

    private View.OnClickListener PendingBtnClickListener;
    private View.OnClickListener addNotesBtnClickListener;
    private View.OnClickListener SaveProgressBtnClickListener;
    private View.OnClickListener DoneBtnClickListener;
    private View.OnClickListener TransitionsBtnClickListener;
    private View.OnClickListener TogBtnClickListener;
    private int PendingBtn = -1;
    private int addNotes = -1;
    private int SaveProgressBtn = -1;
    private int DoneBtn = -1;
    private int TransitionBtn = -1;

    public boxesAdapter getProgListView() {
        return progListView;
    }

    public void setProgListView(boxesAdapter progListView) {
        this.progListView = progListView;
    }

    public View.OnClickListener getTogBtnClickListener() {
        return TogBtnClickListener;
    }

    public void setTogBtnClickListener(View.OnClickListener togBtnClickListener) {
        TogBtnClickListener = togBtnClickListener;
    }

    public List<boxes> getProgressList() {
        return ProgressList;
    }

    public void setProgressList(ArrayList<boxes> progressList) {
        ProgressList = progressList;
    }

    public int getPendingBtn() {
        return PendingBtn;
    }

    public void setPendingBtn(int pendingBtn) {
        PendingBtn = pendingBtn;
    }

    public int getAddNotes() {
        return addNotes;
    }

    public void setAddNotes(int addNotes) {
        this.addNotes = addNotes;
    }

    public int getSaveProgressBtn() {
        return SaveProgressBtn;
    }

    public void setSaveProgressBtn(int saveProgressBtn) {
        SaveProgressBtn = saveProgressBtn;
    }

    public int getDoneBtn() {
        return DoneBtn;
    }

    public void setDoneBtn(int doneBtn) {
        DoneBtn = doneBtn;
    }

    public int getTransitionBtn() {
        return TransitionBtn;
    }

    public void setTransitionBtn(int transitionBtn) {
        TransitionBtn = transitionBtn;
    }

    public View.OnClickListener getTransitionsBtnClickListener() {
        return TransitionsBtnClickListener;
    }

    public void setTransitionsBtnClickListener(View.OnClickListener transitionsBtnClickListener) {
        TransitionsBtnClickListener = transitionsBtnClickListener;
    }

    public View.OnClickListener getAddNotesBtnClickListener() {
        return addNotesBtnClickListener;
    }

    public void setAddNotesBtnClickListener(View.OnClickListener addNotesBtnClickListener) {
        this.addNotesBtnClickListener = addNotesBtnClickListener;
    }

    public View.OnClickListener getSaveProgressBtnClickListener() {
        return SaveProgressBtnClickListener;
    }

    public void setSaveProgressBtnClickListener(View.OnClickListener saveProgressBtnClickListener) {
        SaveProgressBtnClickListener = saveProgressBtnClickListener;
    }

    public View.OnClickListener getDoneBtnClickListener() {
        return DoneBtnClickListener;
    }

    public void setDoneBtnClickListener(View.OnClickListener doneBtnClickListener) {
        DoneBtnClickListener = doneBtnClickListener;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Order(String orderNum, String Date, String Time, String Place, String location, String fixType, String classMatter, String DliverCost, String notes, String file, String username, List<boxes> list) {
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a
        this.orderNum = orderNum;
        this.Date = Date;
        this.Time = Time;
        this.Place = Place;
        this.location = location;
        this.fixType = fixType;
        this.classMatter = classMatter;
        this.DliverCost = DliverCost;
        this.notes = notes;
        this.file = file;
<<<<<<< HEAD
=======
        this.userName = username;
        this.ProgressList = list;
>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a

    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFixType() {
        return fixType;
    }

    public void setFixType(String fixType) {
        this.fixType = fixType;
    }

    public String getClassMatter() {
        return classMatter;
    }

    public void setClassMatter(String classMatter) {
        this.classMatter = classMatter;
    }

    public String getDliverCost() {
        return DliverCost;
    }

    public void setDliverCost(String dliverCost) {
        DliverCost = dliverCost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

<<<<<<< HEAD
=======
    public View.OnClickListener getPendingBtnClickListener() {
        return PendingBtnClickListener;
    }

    public void setPendingBtnClickListener(View.OnClickListener pendingBtnClickListener) {
        this.PendingBtnClickListener = pendingBtnClickListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order item = (Order) o;

        if (orderNum != null ? !orderNum.equals(item.orderNum) : item.orderNum != null) {
            return false;
        }
        if (Date != null ? !Date.equals(item.Date) : item.Date != null) {
            return false;
        }
        if (Time != null ? !Time.equals(item.Time) : item.Time != null) {
            return false;
        }
        if (Place != null ? !Place.equals(item.Place) : item.Place != null) {
            return false;
        }
        if (location != null ? !location.equals(item.location) : item.location != null) {
            return false;
        }
        if (DliverCost != null ? !DliverCost.equals(item.DliverCost) : item.DliverCost != null) {
            return false;
        }
        if (notes != null ? !notes.equals(item.notes) : item.notes != null) {
            return false;
        }

        if (classMatter != null ? !classMatter.equals(item.classMatter) : item.classMatter != null) {
            return false;
        }
        if (file != null ? !file.equals(item.file) : item.file != null) {
            return false;
        }
        return !(fixType != null ? !fixType.equals(item.fixType) : item.fixType != null);

    }

    @Override
    public int hashCode() {
        int result = orderNum != null ? orderNum.hashCode() : 0;
        result = 31 * result + (Date != null ? Date.hashCode() : 0);
        result = 31 * result + (Time != null ? Time.hashCode() : 0);
        result = 31 * result + (Place != null ? Place.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (DliverCost != null ? DliverCost.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (classMatter != null ? notes.hashCode() : 0);
        result = 31 * result + (file != null ? notes.hashCode() : 0);
        result = 31 * result + (fixType != null ? notes.hashCode() : 0);
        return result;
    }

>>>>>>> e657a51ca46385b1f80c980290f93f6b456eb68a
}
