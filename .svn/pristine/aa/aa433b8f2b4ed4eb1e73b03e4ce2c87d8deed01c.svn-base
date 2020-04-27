package com.xiezhiai.wechatplugin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.model.wechat.ChatRoom;
import com.xiezhiai.wechatplugin.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/22.
 *
 * @Desc:
 */
public class SendMsgSelectGroupAdapter extends BaseAdapter {

    private Context context;
    private List<ChatRoom> chatRoomList;

    public SendMsgSelectGroupAdapter(Context context, List<ChatRoom> chatRoomList) {
        this.context = context;
        this.chatRoomList = chatRoomList;
    }

    @Override
    public int getCount() {
        return chatRoomList == null ? 0 : chatRoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatRoomList == null ? null : chatRoomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_send_target_select_chatroom, parent, false);
            holder = new ViewHolder();
            holder.cbChatRoom = convertView.findViewById(R.id.cb_room);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChatRoom chatRoom = chatRoomList.get(position);
        holder.cbChatRoom.setText(chatRoom.getName());
        holder.cbChatRoom.setChecked(chatRoom.isChecked());

        holder.cbChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatRoomList.get(position).setChecked(((CheckBox) v).isChecked());
                boolean allUnselected = true;
                for (int i = 0; i < chatRoomList.size(); i++) {
                    if (chatRoomList.get(i).isChecked()) {
                        allUnselected = false;
                        break;
                    }
                }
                if (selectChatroomListener != null)
                    selectChatroomListener.onAllUnselected(allUnselected);
            }
        });

        return convertView;
    }

    static class ViewHolder {

        CheckBox cbChatRoom;
    }

    public interface SelectChatroomListener {

        void onAllUnselected(boolean all);
    }

    private SelectChatroomListener selectChatroomListener;

    public void setSelectChatroomListener(SelectChatroomListener selectChatroomListener) {
        this.selectChatroomListener = selectChatroomListener;
    }

    public ArrayList<ChatRoom> getSelected() {
        ArrayList<ChatRoom> chatRooms = new ArrayList<>();
        for (int i = 0; i < chatRoomList.size(); i++) {
            if (chatRoomList.get(i).isChecked()) {
                chatRooms.add(chatRoomList.get(i));
            }
        }
        return chatRooms;
    }

    public void cancelSelected() {
        for (int i = 0; i < chatRoomList.size(); i++) {
            chatRoomList.get(i).setChecked(false);
        }
        notifyDataSetChanged();
    }

}
