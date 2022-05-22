package com.example.traficTicketapp_android;

import android.os.Parcel;
import android.os.Parcelable;

public class Offender implements Parcelable {
    String firstName;
    String lastName;
    Integer speedLimitZone;
    Boolean isSchoolOrWorkZone;
    Integer offenderSpeed;
    Integer fineAmount;
    String fineDate;

    public Offender(String firstName, String lastName, Integer speedLimitZone, Boolean isSchoolOrWorkZone, Integer offenderSpeed,
                    int fineAmount, String fineDate){
        this.firstName = firstName;
        this.lastName = lastName;
        this.speedLimitZone = speedLimitZone;
        this.isSchoolOrWorkZone = isSchoolOrWorkZone;
        this.offenderSpeed = offenderSpeed;
        this.fineAmount = fineAmount;
        this.fineDate = fineDate;
    }

    protected Offender(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        if (in.readByte() == 0) {
            speedLimitZone = null;
        } else {
            speedLimitZone = in.readInt();
        }
        byte tmpIsSchoolOrWorkZone = in.readByte();
        isSchoolOrWorkZone = tmpIsSchoolOrWorkZone == 0 ? null : tmpIsSchoolOrWorkZone == 1;
        if (in.readByte() == 0) {
            offenderSpeed = null;
        } else {
            offenderSpeed = in.readInt();
        }
        if (in.readByte() == 0) {
            fineAmount = null;
        } else {
            fineAmount = in.readInt();
        }
        fineDate = in.readString();
    }

    public static final Creator<Offender> CREATOR = new Creator<Offender>() {
        @Override
        public Offender createFromParcel(Parcel in) {
            return new Offender(in);
        }

        @Override
        public Offender[] newArray(int size) {
            return new Offender[size];
        }
    };

    public Integer getSpeedLimitZone() {
        return speedLimitZone;
    }

    public void setSpeedLimitZone(Integer speedLimitZone) {
        this.speedLimitZone = speedLimitZone;
    }

    public Integer getOffenderSpeed() {
        return offenderSpeed;
    }

    public void setOffenderSpeed(Integer offenderSpeed) {
        this.offenderSpeed = offenderSpeed;
    }

    public Boolean getSchoolOrWorkZone() {
        return isSchoolOrWorkZone;
    }

    public void setSchoolOrWorkZone(Boolean schoolOrWorkZone) {
        isSchoolOrWorkZone = schoolOrWorkZone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Integer fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getTicketDate() {
        return fineDate;
    }

    public void setTicketDate(String ticketDate) {
        this.fineDate = ticketDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        if (speedLimitZone == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(speedLimitZone);
        }
        parcel.writeByte((byte) (isSchoolOrWorkZone == null ? 0 : isSchoolOrWorkZone ? 1 : 2));
        if (offenderSpeed == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(offenderSpeed);
        }
        if (fineAmount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(fineAmount);
        }
        parcel.writeString(fineDate);
    }
}
