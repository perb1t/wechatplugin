package com.xiezhiai.wechatplugin.ui.aty;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.adapter.SnsqaListAdapter;
import com.xiezhiai.wechatplugin.func.nohttp.URLManager;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleRequest;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleResult;
import com.xiezhiai.wechatplugin.model.xiezhi.Commonqa;
import com.xiezhiai.wechatplugin.model.xiezhi.Snsqa;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.ui.frg.QALibFragment;
import com.xiezhiai.wechatplugin.utils.DensityUtils;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;
import com.yanzhenjie.nohttp.rest.Response;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/29.
 *
 * @Desc:
 */
public class SnsqaListActivity extends BaseActivity implements CommonTopBar.CommonTopBarListener, View.OnClickListener, SnsqaListAdapter.SnsqaListAdapterListner {

    private CommonTopBar commonTopBar;
    private View container;

    /* empty layer*/
    private View emptyLayer;
    private View btnGetSns;

    /*data layer*/
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView lvSnsqa;
    private View btnAddSnsqa;

    private View menuLyaer;
    private View tipsLyaer;
    private View btnCloseTips;
    private CheckBox cbCheckAll;
    private TextView tvSelectedCount;
    private View btnOpen;
    private View btnClose;
    private View btnDelete;

    private List<Snsqa> snsqaList = new ArrayList<>();
    private List<Snsqa> selectList = new ArrayList<>();
    private SnsqaListAdapter adapter;

    private int pn = 1;
    private int ps = 20;
    private boolean isRefresh = false;
    private boolean isEdit = false;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_snsqa_list;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {
//        snsCount = getIntent().getIntExtra(QALibFragment.KEY_COUNT, 0);
    }

    @Override
    public void initialView() {
        commonTopBar = findViewById(R.id.common_topbar);
        container = findViewById(R.id.container);
        btnGetSns = findViewById(R.id.btn_get_sns);
        emptyLayer = findViewById(R.id.empty_layer);
        smartRefreshLayout = findViewById(R.id.refresh_layout);

        lvSnsqa = findViewById(R.id.lv_snsqa);
        btnAddSnsqa = findViewById(R.id.btn_add_snsqa_ring);

        menuLyaer = findViewById(R.id.layer_buttom_menu);
        tipsLyaer = findViewById(R.id.tips_layer);
        cbCheckAll = findViewById(R.id.cb_check_all);
        tvSelectedCount = findViewById(R.id.tv_selected_count);
        btnOpen = findViewById(R.id.btn_open);
        btnClose = findViewById(R.id.btn_close);
        btnDelete = findViewById(R.id.btn_delete);
        btnCloseTips = findViewById(R.id.btn_close_tips);
//        refreshUI(snsCount == 0);
        adapter = new SnsqaListAdapter(snsqaList);
        adapter.setCommonqaListAdapterListner(this);
        lvSnsqa.setAdapter(adapter);
        lvSnsqa.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, (int) DensityUtils.dp2px(6));
            }
        });


    }

    @Override
    public void initialEvn() {
        commonTopBar.setCommonTopBarListener(this);
        btnGetSns.setOnClickListener(this);
        btnAddSnsqa.setOnClickListener(this);
        cbCheckAll.setOnClickListener(this);
        btnCloseTips.setOnClickListener(this);
        lvSnsqa.setLayoutManager(new LinearLayoutManager(this));
        onSelect(0, null);
        btnDelete.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnOpen.setOnClickListener(this);
        smartRefreshLayout.setOnRefreshLoadMoreListener(onRefreshLoadMoreListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isEdit) return;
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void onBackKeyPressed() {
        if (isEdit){
            edit(false);
        }else {
            finish();
        }
    }

    @Override
    public void onNetworkStateChanged(int type, boolean isAvailable) {

    }

    @Override
    public void onTopLeftButtonClick(View v) {
        if (isEdit){
            edit(false);
        }else {
            finish();
        }
    }

    @Override
    public void onTopRightButtonClick(View v) {
        edit(!isEdit);
    }

    @Override
    public void onSelect(int size, List<Snsqa> select) {
        selectList.clear();
        if (select != null) {
            selectList.addAll(select);
        }
        SpannableString label = new SpannableString("已选 " + size + " 条");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0300"));
        label.setSpan(colorSpan, 3, label.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvSelectedCount.setText(label);
    }

    @Override
    public void onItemClick(int position) {

        if (isEdit) return;
        Snsqa snsqa = snsqaList.get(position);
        Intent intent = new Intent(this, EditeGoodsActivity.class);
        intent.putExtra(EditeGoodsActivity.KEY_IS_EDIT, true);
        intent.putExtra(EditeGoodsActivity.KEY_SNS_qa, snsqa);
        startActivityForResult(intent, URLManager.Update_Goods_qa.action);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_sns:
                Intent sns = new Intent(this, SnsWrapperListActivity.class);
                startActivity(sns);
                break;
            case R.id.cb_check_all:
                cbCheckAll.setChecked(cbCheckAll.isChecked());
                adapter.checkAll(cbCheckAll.isChecked());
                break;
            case R.id.btn_add_snsqa_ring:
                startActivity(new Intent(this, SnsWrapperListActivity.class));
                break;
            case R.id.btn_close_tips:
                tipsLyaer.setVisibility(View.GONE);
                break;
            case R.id.btn_close:
                if (selectList.size() > 0) {
                    String[] listclose = new String[selectList.size()];
                    for (int i = 0; i < selectList.size(); i++) {
                        String goodsId = selectList.get(i).getId();
                        listclose[i] = goodsId;
                    }
                    deleteGoodList("disable", listclose);
                } else {
                    showMsg("请选择问答");
                }

                break;
            case R.id.btn_delete:
                if (selectList.size() > 0) {
                    String[] listdelete = new String[selectList.size()];
                    for (int i = 0; i < selectList.size(); i++) {
                        String goodsId = selectList.get(i).getId();
                        listdelete[i] = goodsId;
                    }
                    deleteGoodList("delete", listdelete);
                } else {
                    showMsg("请选择问答");
                }

                break;
            case R.id.btn_open:
                if (selectList.size() > 0) {
                    String[] listopen = new String[selectList.size()];
                    for (int i = 0; i < selectList.size(); i++) {
                        String goodsId = selectList.get(i).getId();
                        listopen[i] = goodsId;
                    }
                    deleteGoodList("enable", listopen);
                } else {
                    showMsg("请选择问答");
                }
                break;
        }
    }

    private OnRefreshLoadMoreListener onRefreshLoadMoreListener = new OnRefreshLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            pn++;
            isRefresh = false;
            getGoodsList(ps, pn);
        }

        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            pn = 1;
            isRefresh = true;
            getGoodsList(ps, pn);
        }
    };

    @Override
    public void onHttpSucceed(int what, SimpleResult ret, Response response) {
        super.onHttpSucceed(what, ret, response);

        if (ret != null) {
            if (what == URLManager.Delete_Goods_qa.action) {
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    cbCheckAll.setChecked(false);
                    isRefresh = true;
                    pn = 1;
                    getGoodsList(ps, pn);
                } else {
                    showMsg(ret.getMessage());
                }
            } else if (what == URLManager.Get_Goods_qa_List.action) {

                if (ret.getCode() == SimpleResult.SUCCESS) {
                    JSONObject data = JSON.parseObject(ret.getData());
                    if (data != null) {
                        JSONArray goods_list = data.getJSONArray("goods_list");
                        if (goods_list != null) {
                            if (isRefresh) snsqaList.clear();
                            snsqaList.addAll(parsSnsList(goods_list));
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    showMsg(ret.getMessage());
                }
                isEmpty(snsqaList.size() == 0);
            }

        }
    }

    @Override
    public void onHttpFailed(int what, SimpleResult ret, Response response) {
        super.onHttpFailed(what, ret, response);
        if (ret != null) {
            showMsg(ret.getMessage());
        }
    }

    @Override
    public void onHttpFinish(int what) {
        super.onHttpFinish(what);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }


    private List<Snsqa> parsSnsList(JSONArray goods_list) {
        List<Snsqa> result = new ArrayList<>();
        if (goods_list != null) {
            for (int i = 0; i < goods_list.size(); i++) {
                JSONObject jsonObject = goods_list.getJSONObject(i);
                String s = jsonObject.toJSONString();
                Snsqa snsqa = JSON.parseObject(s, Snsqa.class);
                result.add(snsqa);
            }
        }
        return result;
    }

    /**
     * 获取朋友圈商品问答列表
     *
     * @param ps
     * @param pn
     */
    private void getGoodsList(int ps, int pn) {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Get_Goods_qa_List.getURL() + "?pn=" + pn + "&ps=" + ps, URLManager.Get_Goods_qa_List.method, SimpleResult.class);
        addTask2Queue(
                URLManager.Get_Goods_qa_List.action,
                request
        );

    }

    /**
     * 更新朋友圈商品问答列表
     *
     * @param type
     * @param list
     */
    private void deleteGoodList(String type, String... list) {
        edit(false);
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

    /**
     * 是否空数据切换页面
     *
     * @param isEmpty
     */
    private void isEmpty(boolean isEmpty) {
        emptyLayer.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        commonTopBar.getTopRightButton().setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        smartRefreshLayout.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        if (!isEdit) btnAddSnsqa.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    /**
     * 是否编辑切换页面
     *
     * @param isEdit
     */
    private void edit(boolean isEdit) {
        this.isEdit = isEdit;
        adapter.isEdit(isEdit);
        menuLyaer.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        btnAddSnsqa.setVisibility(isEdit ? View.GONE : View.VISIBLE);
        commonTopBar.getTopRightButton().setText(isEdit ? "完成" : "编辑");

        SmartRefreshLayout.LayoutParams lp = (SmartRefreshLayout.LayoutParams) lvSnsqa.getLayoutParams();
        lp.bottomMargin = isEdit ? (int) DensityUtils.dp2px(2) : 0;
        LinearLayout.LayoutParams clp = (LinearLayout.LayoutParams) container.getLayoutParams();
        clp.setMargins(
                isEdit ? 0 : (int) DensityUtils.dp2px(10),
                (int) DensityUtils.dp2px(10),
                isEdit ? 0 : (int) DensityUtils.dp2px(10),
                isEdit ? 0 : (int) DensityUtils.dp2px(10));

        if (!isEdit) {
            adapter.checkAll(false);
            cbCheckAll.setChecked(false);
        }

    }
}
