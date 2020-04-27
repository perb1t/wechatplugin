package com.xiezhiai.wechatplugin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.model.wechat.UserInfo;
import com.xiezhiai.wechatplugin.model.xiezhi.WXUser;
import com.xiezhiai.wechatplugin.utils.VerifyUtil;
import com.xiezhiai.wechatplugin.utils.image.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/23.
 *
 * @Desc:
 */
public class WechatUserAccountAdapter extends BaseAdapter {

    private List<WXUser> wxUsers;
    private Context context;
    private boolean isEdit;

    public WechatUserAccountAdapter(List<WXUser> userInfoList, Context context) {
        this.wxUsers = userInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return wxUsers == null ? null : wxUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return wxUsers == null ? null : wxUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final WXUser entity = wxUsers.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_account, null);
            holder = new ViewHolder();
            holder.ivAvatar = convertView.findViewById(R.id.iv_wechat_avatar);
            holder.tvUserName = convertView.findViewById(R.id.tv_wechat_name);
            holder.tvUserId = convertView.findViewById(R.id.tv_wechat_id);
            holder.ivLoginMark = convertView.findViewById(R.id.iv_login_maker);
            holder.btnUnbind = convertView.findViewById(R.id.btn_unbind);
            holder.layoutUnbind = convertView.findViewById(R.id.layout_unbind);
            holder.wxUserLayer = convertView.findViewById(R.id.layer_wx_user);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivLoginMark.setVisibility(View.GONE);
        holder.layoutUnbind.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        ImageLoader.with(context).load(entity.getAvatar(), holder.ivAvatar, R.mipmap.ic_avatar_placeholder);
        holder.tvUserId.setText("ID: " + entity.getId());
        holder.tvUserName.setText(entity.getName());
        if (!isEdit)
            holder.ivLoginMark.setVisibility(entity.isInServic() ? View.VISIBLE : View.GONE);

        holder.layoutUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* delete wechat */
                if (bindedWXUserListener != null) bindedWXUserListener.onUnbind(entity, position);
            }
        });

        holder.wxUserLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 切换服务的微信号 */
                if (bindedWXUserListener != null)
                    bindedWXUserListener.onSwitchServiceWXUser(entity, position);
            }
        });
        return convertView;
    }

    static class ViewHolder {

        ImageView ivAvatar;
        TextView tvUserName;
        TextView tvUserId;
        ImageView ivLoginMark;
        View btnUnbind;
        View layoutUnbind;
        View wxUserLayer;

    }

    /**
     * @param edit
     */
    public void edit(boolean edit) {
        this.isEdit = edit;
        notifyDataSetChanged();
    }

    public interface BindedWXUserListener {

        void onUnbind(WXUser wxUser, int position);

        void onSwitchServiceWXUser(WXUser wxUser, int position);
    }

    private BindedWXUserListener bindedWXUserListener;

    public void setBindedWXUserListener(BindedWXUserListener bindedWXUserListener) {
        this.bindedWXUserListener = bindedWXUserListener;
    }
}
