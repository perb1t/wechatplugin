package com.xiezhiai.wechatplugin.model.xiezhi;

import android.os.Parcel;
import android.os.Parcelable;

import com.xiezhiai.wechatplugin.utils.sns.bean.SnsInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/11/1.
 *
 * @Desc:
 */
public class SnsWrapper implements Parcelable {

    private SnsInfo snsInfo;

    private ArrayList<Snsqa> snsqas = new ArrayList<>();

    private boolean isExpand;

    public SnsWrapper() {
    }

    public SnsWrapper(SnsInfo snsInfo, ArrayList<Snsqa> snsqas) {
        this.snsInfo = snsInfo;
        if (snsqas != null && snsqas.size() != 0)
            this.snsqas = snsqas;
    }

    public SnsInfo getSnsInfo() {
        return snsInfo;
    }

    public void setSnsInfo(SnsInfo snsInfo) {
        this.snsInfo = snsInfo;
    }

    public ArrayList<Snsqa> getSnsqas() {
        return snsqas;
    }

    public void setSnsqas(ArrayList<Snsqa> snsqas) {
        if (snsqas != null && snsqas.size() != 0) {
            this.snsqas.clear();
            this.snsqas = snsqas;
        }
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.snsInfo, flags);
        dest.writeList(this.snsqas);
        dest.writeByte(this.isExpand ? (byte) 1 : (byte) 0);
    }

    protected SnsWrapper(Parcel in) {
        this.snsInfo = in.readParcelable(SnsInfo.class.getClassLoader());
        this.snsqas = new ArrayList<Snsqa>();
        in.readList(this.snsqas, Snsqa.class.getClassLoader());
        this.isExpand = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SnsWrapper> CREATOR = new Parcelable.Creator<SnsWrapper>() {
        @Override
        public SnsWrapper createFromParcel(Parcel source) {
            return new SnsWrapper(source);
        }

        @Override
        public SnsWrapper[] newArray(int size) {
            return new SnsWrapper[size];
        }
    };
}
