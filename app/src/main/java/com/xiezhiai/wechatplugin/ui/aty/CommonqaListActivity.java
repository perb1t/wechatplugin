package com.xiezhiai.wechatplugin.ui.aty;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.xiezhiai.wechatplugin.adapter.CommonqaListAdapter;
import com.xiezhiai.wechatplugin.func.nohttp.URLManager;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleRequest;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleResult;
import com.xiezhiai.wechatplugin.model.xiezhi.Commonqa;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.ui.frg.QALibFragment;
import com.xiezhiai.wechatplugin.utils.DensityUtils;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by shijiwei on 2018/10/29.
 *
 * @Desc:
 */
public class CommonqaListActivity extends BaseActivity implements CommonTopBar.CommonTopBarListener, View.OnClickListener, CommonqaListAdapter.CommonqaListAdapterListner {

    private CommonTopBar commonTopBar;
    private SmartRefreshLayout smartRefreshLayout;
    private View btnAddCommonqa;
    private View btnRingAddCommonqa;
    private View emptyLayer;
    private View menuLayer;
    private RecyclerView lvCommonqa;
    private View container;

    /* menu */
    private CheckBox cbCheckAll;
    private TextView tvSelectedCount;
    private View btnOpen;
    private View btnClose;
    private View btnDelete;

    private int pn = 1;
    private int ps = 20;
    private boolean isRefresh = false;
    private boolean isEdit = false;

    private List<Commonqa> commonqaList = new ArrayList<>();
    private CommonqaListAdapter adapter;
    private List<Commonqa> selectList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_commonqa_list;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {
//        commonqaCount = getIntent().getIntExtra(QALibFragment.KEY_COUNT, 0);
    }

    @Override
    public void initialView() {
        commonTopBar = findViewById(R.id.common_topbar);
        smartRefreshLayout = findViewById(R.id.refresh_layout);
        btnAddCommonqa = findViewById(R.id.btn_add_commonqa);
        btnRingAddCommonqa = findViewById(R.id.btn_add_commonqa_ring);
        emptyLayer = findViewById(R.id.empty_layer);
        menuLayer = findViewById(R.id.layer_buttom_menu);
        lvCommonqa = findViewById(R.id.lv_commonqa);
        container = findViewById(R.id.container);

        cbCheckAll = findViewById(R.id.cb_check_all);
        tvSelectedCount = findViewById(R.id.tv_selected_count);
        btnOpen = findViewById(R.id.btn_open);
        btnClose = findViewById(R.id.btn_close);
        btnDelete = findViewById(R.id.btn_delete);


        adapter = new CommonqaListAdapter(commonqaList);
        lvCommonqa.setLayoutManager(new LinearLayoutManager(this));
        lvCommonqa.setAdapter(adapter);
        lvCommonqa.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, (int) DensityUtils.dp2px(5));
            }
        });
    }

    @Override
    public void initialEvn() {
        commonTopBar.setCommonTopBarListener(this);
        btnAddCommonqa.setOnClickListener(this);
        btnRingAddCommonqa.setOnClickListener(this);
        cbCheckAll.setOnClickListener(this);
        adapter.setCommonqaListAdapterListner(this);
        btnOpen.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        onSelect(0, null);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pn++;
                isRefresh = false;
                getCommonQaQuestion(pn, ps);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pn = 1;
                isRefresh = true;
                getCommonQaQuestion(pn, ps);
            }
        });
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
    public void onSelect(int size, List<Commonqa> select) {
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
    public void onItemClick(int postion) {

        if (isEdit) return;
        Commonqa commonqa = commonqaList.get(postion);
        String questionId = commonqa.getQuestionId();
        String title = commonqa.getTitle();
        String content = commonqa.getContent();
        int status = commonqa.getStatus();
        Intent intent = new Intent();
        intent.setClass(this, AddCommonqaActivity.class);
        intent.putExtra("isupdate", true);
        intent.putExtra("question", title);
        intent.putExtra("answer", content);
        intent.putExtra("status", status);
        intent.putExtra("questionId", questionId);
        startActivityForResult(intent, 101);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_check_all:
                cbCheckAll.setChecked(cbCheckAll.isChecked());
                adapter.checkAll(cbCheckAll.isChecked());
                break;
            case R.id.btn_add_commonqa:
                startActivityForResult(new Intent(this, AddCommonqaActivity.class), 101);
                break;
            case R.id.btn_add_commonqa_ring:
                startActivityForResult(new Intent(this, AddCommonqaActivity.class), 101);
                break;
            case R.id.btn_open:
                if (selectList.size() == 0) {
                    showMsg("请选择问答");
                    return;
                }
                String[] listopen = new String[selectList.size()];
                for (int i = 0; i < selectList.size(); i++) {
                    String questionId = selectList.get(i).getQuestionId();
                    listopen[i] = questionId;
                }
                deleteCommonQaQuestion("enable", listopen);
                break;
            case R.id.btn_close:
                if (selectList.size() == 0) {
                    showMsg("请选择问答");
                    return;
                }
                String[] listclose = new String[selectList.size()];
                for (int i = 0; i < selectList.size(); i++) {
                    String questionId = selectList.get(i).getQuestionId();
                    listclose[i] = questionId;
                }
                deleteCommonQaQuestion("disable", listclose);
                break;
            case R.id.btn_delete:
                if (selectList.size() == 0) {
                    showMsg("请选择问答");
                    return;
                }
                String[] listdelete = new String[selectList.size()];
                for (int i = 0; i < selectList.size(); i++) {
                    String questionId = selectList.get(i).getQuestionId();
                    listdelete[i] = questionId;
                }
                deleteCommonQaQuestion("delete", listdelete);
                break;
        }

    }

    private List<Commonqa> parserCommonqaList(JSONArray question_list) {
        List<Commonqa> result = new ArrayList<>();
        Commonqa commonqa = null;
        if (question_list != null) {
            for (int i = 0; i < question_list.size(); i++) {
                JSONObject jsonObject = question_list.getJSONObject(i);
                int status = jsonObject.getInteger("status");
                if (status == 0) {
                    commonqa = new Commonqa(false, null);
                    commonqa.setStatus(status);
                } else {
                    commonqa = new Commonqa(true, null);
                    commonqa.setStatus(status);
                }
                commonqa.setQuestionId(jsonObject.getString("question_id"));
                commonqa.setTitle(jsonObject.getString("question"));
                commonqa.setContent(jsonObject.getString("answer"));
                result.add(commonqa);
            }
        }
        return result;
    }

    @Override
    public void onHttpFailed(int what, SimpleResult ret, Response response) {
        super.onHttpFailed(what, ret, response);
        if (ret != null) {
            showMsg(ret.getMessage());
        }

    }

    @Override
    public void onHttpSucceed(int what, SimpleResult ret, Response response) {
        super.onHttpSucceed(what, ret, response);
        if (ret != null) {
            if (what == URLManager.Delete_Common_qa.action) {
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    cbCheckAll.setChecked(false);
                    pn = 1;
                    isRefresh = true;
                    getCommonQaQuestion(pn, ps);
                } else {
                    showMsg(ret.getMessage());
                }
            } else if (what == URLManager.Get_Common_qa_List.action) {
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    JSONObject data = JSON.parseObject(ret.getData());
                    if (data != null) {
                        JSONArray question_list = data.getJSONArray("question_list");
                        if (isRefresh) commonqaList.clear();
                        commonqaList.addAll(parserCommonqaList(question_list));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showMsg(ret.getMessage());
                }
                isEmpty(commonqaList.size() == 0);
            }
        }
    }

    @Override
    public void onHttpFinish(int what) {
        super.onHttpFinish(what);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    /**
     * 获取通用问答列表
     *
     * @param pn
     * @param ps
     */
    public void getCommonQaQuestion(int pn, int ps) {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Get_Common_qa_List.getURL() + "?pn=" + pn + "&ps=" + ps, URLManager.Get_Common_qa_List.method, SimpleResult.class);
        addTask2Queue(
                URLManager.Get_Common_qa_List.action,
                request
        );
    }

    /**
     * 删除通用问答列表
     *
     * @param type
     * @param list
     */
    public void deleteCommonQaQuestion(String type, String... list) {
        edit(false);
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Delete_Common_qa.getURL(), URLManager.Delete_Common_qa.method, SimpleResult.class);
        HashMap<String, Object> p = new HashMap<>();
        p.put("type", type);
        p.put("question_id_list", list);
        request.setRequestBodyAsJson(p);
        addTask2Queue(
                URLManager.Delete_Common_qa.action,
                request
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pn = 1;
        isRefresh = true;
        getCommonQaQuestion(pn, ps);
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
        if (!isEdit) btnRingAddCommonqa.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    /**
     * 是否编辑切换页面
     *
     * @param isEdit
     */
    private void edit(boolean isEdit) {
        this.isEdit = isEdit;
        adapter.isEdit(isEdit);
        menuLayer.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        btnRingAddCommonqa.setVisibility(isEdit ? View.GONE : View.VISIBLE);
        commonTopBar.getTopRightButton().setText(isEdit ? "完成" : "编辑");

        SmartRefreshLayout.LayoutParams lp = (SmartRefreshLayout.LayoutParams) lvCommonqa.getLayoutParams();
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
