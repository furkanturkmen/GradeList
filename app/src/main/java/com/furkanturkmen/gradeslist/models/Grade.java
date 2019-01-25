package com.furkanturkmen.gradeslist.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "grade")

public class Grade implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "gradeText")
    private String gradeText;
    @ColumnInfo(name = "gradeScore")
    private String gradeScore;
    @ColumnInfo(name = "gradeDate")
    private String gradeDate;

    public Grade(String gradeText, String gradeScore, String gradeDate) {
        this.gradeText = gradeText;
        this.gradeScore = gradeScore;
        this.gradeDate = gradeDate;
    }

    public Grade(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGradeText() {
        return gradeText;
    }

    public void setGradeText(String gradeText) {
        this.gradeText = gradeText;
    }

    public String getGradeScore() {
        return gradeScore;
    }

    public void setGradeScore(String gradeScore) {
        this.gradeScore = gradeScore;
    }

    public String getGradeDate() {
        return gradeDate;
    }

    public void setGradeDate(String gradeDate) {
        this.gradeDate = gradeDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.gradeText);
        dest.writeString(this.gradeScore);
        dest.writeString(this.gradeDate);
    }

    protected Grade(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.gradeText = in.readString();
        this.gradeScore = in.readString();
        this.gradeDate = in.readString();
    }

    public static final Creator<Grade> CREATOR = new Creator<Grade>() {
        @Override
        public Grade createFromParcel(Parcel source) {
            return new Grade(source);
        }

        @Override
        public Grade[] newArray(int size) {
            return new Grade[size];
        }
    };
}

