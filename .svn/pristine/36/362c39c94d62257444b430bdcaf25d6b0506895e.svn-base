package com.xiezhiai.wechatplugin.ui.aty;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.adapter.GenerateGoodsAdapter;

import com.xiezhiai.wechatplugin.func.nohttp.URLManager;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleRequest;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleResult;
import com.xiezhiai.wechatplugin.model.xiezhi.SnsWrapper;

import com.xiezhiai.wechatplugin.model.xiezhi.Snsqa;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.utils.DensityUtils;

import com.xiezhiai.wechatplugin.widget.CommonTopBar;
import com.xiezhiai.wechatplugin.widget.CommonDilalog;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.HashMap;


public class GenerateGoodsActivity extends BaseActivity implements View.OnClickListener, CommonTopBar.CommonTopBarListener, GenerateGoodsAdapter.GenerateGoodsAdapterListener, CommonDilalog.CommonDilalogListener {

    public static final String KEY_DATA = "snsWrapper";
    public static final String KEY_SNS_qa = "snsqa";
    public static final String KEY_SNSWRAPEPR_INDEX = "snsWrapper_index";

    private CommonTopBar commonTopBar;
    private RecyclerView lvSnsWrapper;
    private SnsWrapper snsWrapper;
    private int snsWrapperIndex;
    private GenerateGoodsAdapter adapter;

    private int[] position = new int[2];

    @Override
    public int getLayoutResId() {
        return R.layout.activity_generate_goods;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {
        snsWrapper = getIntent().getParcelableExtra(KEY_DATA);
        snsWrapperIndex = getIntent().getIntExtra(KEY_SNSWRAPEPR_INDEX, 0);
    }

    @Override
    public void initialView() {
        commonTopBar = findViewById(R.id.common_topbar);
        lvSnsWrapper = findViewById(R.id.lv_sns_wrapper);
        lvSnsWrapper.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GenerateGoodsAdapter(snsWrapper);
        lvSnsWrapper.setAdapter(adapter);
        lvSnsWrapper.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set((int) DensityUtils.dp2px(10), 0, (int) DensityUtils.dp2px(10), (int) DensityUtils.dp2px(10));
            }
        });
    }

    @Override
    public void initialEvn() {
        commonTopBar.setCommonTopBarListener(this);
        adapter.setGenerateGoodsAdapterListener(this);
        commonDilalog.setCommonDilalogListener(this);

    }

    @Override
    public void onBackKeyPressed() {
        setResult(snsWrapper.getSnsqas());
    }


    @Override
    public void onNetworkStateChanged(int type, boolean isAvailable) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTopLeftButtonClick(View v) {
        setResult(snsWrapper.getSnsqas());
    }

    @Override
    public void onTopRightButtonClick(View v) {

    }

    @Override
    public void onDeleteSnsqa(int position) {
        this.position[0] = position;
        commonDilalog.show("确认删除该条商品信息?", "删除");
    }

    @Override
    public void onEditSnsqa(int position) {
        this.position[1] = position;
        Intent intent = new Intent(this, EditeGoodsActivity.class);
        intent.putExtra(EditeGoodsActivity.KEY_IS_EDIT, true);
        intent.putExtra(EditeGoodsActivity.KEY_SNS_qa, snsWrapper.getSnsqas().get(position - 1));
        startActivityForResult(intent, URLManager.Update_Goods_qa.action);
    }

    @Override
    public void onTopCancel(CommonDilalog dilalog) {
        dilalog.dismiss();
    }

    @Override
    public void onCancel(CommonDilalog dilalog) {
        dilalog.dismiss();
    }

    @Override
    public void onConfim(CommonDilalog dilalog) {
        dilalog.dismiss();
        deleteGoodList("delete", snsWrapper.getSnsqas().get(position[0] - 1).getId());
    }

    @Override
    public void onHttpSucceed(int what, SimpleResult ret, Response response) {
        super.onHttpSucceed(what, ret, response);
        if (ret != null) {
            if (what == URLManager.Delete_Goods_qa.action) {
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    snsWrapper.getSnsqas().remove(position[0] - 1);
                    adapter.notifyItemRemoved(position[0] - 1);
                    adapter.notifyDataSetChanged();
                    showMsg(ret.getMessage());
                } else {
                    showMsg(ret.getMessage());
                }
            }
        }
    }

    @Override
    public void onHttpFailed(int what, SimpleResult ret, Response response) {
        super.onHttpFailed(what, ret, response);
        if (ret == null) {
            showMsg(ret.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == RESULT_OK) {
                if (requestCode == URLManager.Add_Goods_qa.action) {
                    Snsqa qa = data.getParcelableExtra(KEY_SNS_qa);
                    if (qa != null) {
                        snsWrapper.getSnsqas().add(qa);
                        adapter.notifyDataSetChanged();
                    }
                } else if (requestCode == URLManager.Update_Goods_qa.action) {
                    Snsqa qa = data.getParcelableExtra(KEY_SNS_qa);
                    if (qa != null) {
                        snsWrapper.getSnsqas().set(position[1] - 1, qa);
//                        adapter.notifyItemChanged(position[1] - 1);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void setResult(ArrayList<Snsqa> qas) {
        Intent intent = getIntent();
        intent.putParcelableArrayListExtra(GenerateGoodsActivity.KEY_SNS_qa, qas);
        intent.putExtra(GenerateGoodsActivity.KEY_SNSWRAPEPR_INDEX, snsWrapperIndex);
        setResult(RESULT_OK, intent);
        finish();
    }


    /**
     * 更新朋友圈商品问答列表
     *
     * @param type
     * @param list
     */
    private void deleteGoodList(String type, String... list) {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Delete_Goods_qa.getURL(), URLManager.Delete_Goods_qa.method, SimpleResult.class);
        HashMap<String, Object> p = new HashMap<>();
        p.put("type", type);
        p.put("goods_id_list", list);
        request.setRequestBodyAsJson(p);
        addTask2Queue(
                URLManager.Delete_Goods_qa.action,
                request
        );
    }

}
