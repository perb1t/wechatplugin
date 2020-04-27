package com.xiezhiai.wechatplugin.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.adapter.AddPhotoAdapter;
import com.xiezhiai.wechatplugin.func.nohttp.URLManager;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleRequest;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleResult;
import com.xiezhiai.wechatplugin.model.xiezhi.Commonqa;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.utils.Gallery;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.utils.VerifyUtil;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/29.
 *
 * @Desc:
 */
public class AddCommonqaActivity extends BaseActivity implements CommonTopBar.CommonTopBarListener, AddPhotoAdapter.AddPhotoListener, View.OnClickListener {

    private CommonTopBar commonTopBar;

    private RecyclerView lvAddPhoto;
    private AddPhotoAdapter adapter;
    private Button btnSend;
    private EditText etQuestion;
    private EditText etAnswer;
    private CheckBox cbIsOpen;
    public boolean isUpdate;
    private String questionId;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_common_qa;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {

    }

    @Override
    public void initialView() {
        commonTopBar = findViewById(R.id.common_topbar);
        lvAddPhoto = findViewById(R.id.lv_add_photo);
        lvAddPhoto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new AddPhotoAdapter();
        lvAddPhoto.setAdapter(adapter);
        adapter.setAddPhotoListener(this);
        btnSend = findViewById(R.id.btn_send);
        etQuestion = findViewById(R.id.et_question);
        etAnswer = findViewById(R.id.et_answer);
        cbIsOpen = findViewById(R.id.cb_isOpen);

        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra("isupdate", false);
        if (isUpdate) {
            String question = intent.getStringExtra("question");
            String answer = intent.getStringExtra("answer");
            int status = intent.getIntExtra("status", -1);
            questionId = intent.getStringExtra("questionId");
            etQuestion.setText(question);
            etAnswer.setText(answer);
            if (status == 0) {
                cbIsOpen.setChecked(false);
            } else {
                cbIsOpen.setChecked(true);
            }
        }
    }

    @Override
    public void initialEvn() {
        commonTopBar.setCommonTopBarListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onBackKeyPressed() {
        finish();
    }

    @Override
    public void onNetworkStateChanged(int type, boolean isAvailable) {

    }

    @Override
    public void onTopLeftButtonClick(View v) {
        finish();
    }

    @Override
    public void onTopRightButtonClick(View v) {

    }

    @Override
    public void onSelectPhotoFromSystemAlbum() {
//        boolean denied = AndPermission.hasAlwaysDeniedPermission(this, Permission.Group.CAMERA);
//        if (denied) {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> strings) {
                        Gallery.openSysAlbum(AddCommonqaActivity.this);
                    }
                }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> strings) {
                showMsg("请允许相关权限，否则无法正常使用功能!");
            }
        }).start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Gallery.ALBUM_RESULT_CODE:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (isUpdate) {
                    Commonqa commonqa = null;
                    VerifyUtil.Result question = VerifyUtil.isEmptyInput(etQuestion);
                    VerifyUtil.Result answer = VerifyUtil.isEmptyInput(etAnswer);
                    int status = 0;
                    if (cbIsOpen.isChecked()) {
                        status = 1;
                        commonqa = new Commonqa(true, null);
                        commonqa.setStatus(status);

                    } else {
                        status = 0;
                        commonqa = new Commonqa(false, null);
                        commonqa.setStatus(status);
                    }
                    if (question.isEmpty) {
                        showMsg("问题为空！");
                        return;
                    }
                    if (answer.isEmpty) {
                        showMsg("答案为空！");
                        return;
                    }
                    commonqa.setTitle(question.value);
                    commonqa.setContent(answer.value);
                    commonqa.setQuestionId(questionId);
                    List<Commonqa> list = new ArrayList<>();
                    list.add(commonqa);
                    UpdateCommonQa(list);
                    LogUtil.i("普通问答更新" + questionId);

                } else {
                    VerifyUtil.Result question = VerifyUtil.isEmptyInput(etQuestion);
                    VerifyUtil.Result answer = VerifyUtil.isEmptyInput(etAnswer);
                    int status = 0;
                    if (cbIsOpen.isChecked()) {
                        status = 1;
                    } else {
                        status = 0;
                    }
                    if (question.isEmpty) {
                        showMsg("问题为空！");
                        return;
                    }
                    if (answer.isEmpty) {
                        showMsg("答案为空！");
                        return;
                    }
                    AddCommonQa(question.value, answer.value, status);
                    LogUtil.i("添加普通问答");
                }
                break;
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
    public void onHttpSucceed(int what, SimpleResult ret, Response response) {
        super.onHttpSucceed(what, ret, response);
        if (ret != null) {
            if (what == URLManager.Update_Common_qa.action) {
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    setResult(102);
                    finish();
                }
                showMsg(ret.getMessage());
            } else if (what == URLManager.Add_Common_qa.action) {
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    setResult(102);
                    finish();
                }
                showMsg(ret.getMessage());
            }
        }
    }

    //添加普通问答
    private void AddCommonQa(String question, String answer, int status) {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Add_Common_qa.getURL(), URLManager.Add_Common_qa.method, SimpleResult.class);
        HashMap<String, Object> p = new HashMap<>();
        p.put("question", question);
        p.put("answer", answer);
        p.put("status", status);
        request.setRequestBodyAsJson(p);
        addTask2Queue(
                URLManager.Add_Common_qa.action,
                request
        );
    }

    //普通问答更新
    private void UpdateCommonQa(List<Commonqa> list) {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Update_Common_qa.getURL(), URLManager.Update_Common_qa.method, SimpleResult.class);
        HashMap<String, Object> p = new HashMap<>();
        String s = JSON.toJSONString(list);
        p.put("question_list", s);
        request.setRequestBodyAsJson(p);
        addTask2Queue(
                URLManager.Update_Common_qa.action,
                request
        );
    }
}
