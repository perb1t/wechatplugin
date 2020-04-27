package com.xiezhiai.wechatplugin.ui.aty;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.adapter.SnsPhotoAdapter;
import com.xiezhiai.wechatplugin.func.nohttp.URLManager;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleRequest;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleResult;
import com.xiezhiai.wechatplugin.model.xiezhi.Snsqa;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.utils.DensityUtils;
import com.xiezhiai.wechatplugin.utils.VerifyUtil;
import com.xiezhiai.wechatplugin.utils.image.loader.ImageLoader;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;
import com.yanzhenjie.nohttp.BitmapBinary;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

public class EditeGoodsActivity extends BaseActivity implements CommonTopBar.CommonTopBarListener, View.OnClickListener, SnsPhotoAdapter.SnsPhotoListener {

    public static final String KEY_SNS_qa = "snsqa";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_SNS_ID = "sns_id";
    public static final String KEY_IS_EDIT = "is_edit";

    private CommonTopBar commonTopBar;
    private RecyclerView lvSnsPhoto;
    private EditText etName;
    private EditText etPrice;
    private EditText etPostage;
    private EditText etAfterSale;
    private EditText etDetail;
    private View btnSave;

    private List<SnsPhotoAdapter.Photo> photos = new ArrayList<>();
    private SnsPhotoAdapter adapter;
    private String snsId;
    private List<String> wxPhotoURL = new ArrayList<>();
    private List<String> addPhotoURL = new ArrayList<>();

    private boolean isEdit = false;
    private Snsqa snsqa = new Snsqa();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_edite_goods;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra(KEY_IS_EDIT, false);
        snsqa = intent.getParcelableExtra(KEY_SNS_qa);
        snsId = intent.getStringExtra(KEY_SNS_ID);
        ArrayList<String> mediaList = intent.getStringArrayListExtra(KEY_PHOTO);


        /* 初始化 */
        if (snsqa == null) snsqa = new Snsqa();
        if (!isEdit) {
            /* 新增 */
            wxPhotoURL.addAll(mediaList);
            for (int i = 0; i < wxPhotoURL.size(); i++) {
                photos.add(new SnsPhotoAdapter.Photo(wxPhotoURL.get(i)));
            }
            snsqa.setSnsId(snsId);
            snsqa.setWxPhotoURL(wxPhotoURL);

        } else {
            /* 修改 */
            for (int i = 0; i < snsqa.getPhotos().size(); i++) {
                photos.add(new SnsPhotoAdapter.Photo(snsqa.getPhotos().get(i).getWxURL()));
                wxPhotoURL.add(snsqa.getPhotos().get(i).getWxURL());
            }
        }
    }

    @Override
    public void initialView() {

        commonTopBar = findViewById(R.id.common_topbar);
        etName = findViewById(R.id.et_goods_name);
        etPrice = findViewById(R.id.et_goods_price);
        etPostage = findViewById(R.id.et_goods_postage);
        etAfterSale = findViewById(R.id.et_goods_aftersales);
        etDetail = findViewById(R.id.et_goods_desc);
        btnSave = findViewById(R.id.btn_save);
        etPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        lvSnsPhoto = findViewById(R.id.lv_snn_photo);
        adapter = new SnsPhotoAdapter(photos, true);
        lvSnsPhoto.setLayoutManager(new GridLayoutManager(this, 3));
        lvSnsPhoto.setAdapter(adapter);
        lvSnsPhoto.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int i = parent.indexOfChild(view);
                outRect.set(0, (int) DensityUtils.dp2px(5), 0, 0);
            }
        });

        etName.setText(snsqa.getName());
        etPrice.setText(snsqa.getPrice());
        etPostage.setText(snsqa.getPostage());
        etAfterSale.setText(snsqa.getAfterSale());
        etDetail.setText(snsqa.getDescribe());

    }

    @Override
    public void initialEvn() {
        commonTopBar.setCommonTopBarListener(this);
        btnSave.setOnClickListener(this);
        adapter.setSnsPhotoListener(this);
        etName.requestFocus();

    }

    @Override
    public void onBackKeyPressed() {
        finish();
    }

    @Override
    public void onNetworkStateChanged(int type, boolean isAvailable) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_save:

                VerifyUtil.Result name = VerifyUtil.isEmptyInput(etName);
                VerifyUtil.Result price = VerifyUtil.isEmptyInput(etPrice);
                VerifyUtil.Result postage = VerifyUtil.isEmptyInput(etPostage);
                VerifyUtil.Result afterSale = VerifyUtil.isEmptyInput(etAfterSale);
                VerifyUtil.Result detail = VerifyUtil.isEmptyInput(etDetail);

                if (name.isEmpty) {
                    showMsg("请输入商品名称");
                    return;
                }

                if (price.isEmpty) {
                    showMsg("请输入商品价格");
                    return;
                }

                if (postage.isEmpty) {
                    showMsg("请输入商品邮费");
                    return;
                }

                if (afterSale.isEmpty) {
                    showMsg("请输入商品售后服务信息");
                    return;
                }

                if (detail.isEmpty) {
                    showMsg("请输入商品详情信息");
                    return;
                }

                snsqa.setName(name.value);
                snsqa.setDescribe(detail.value);
                snsqa.setPrice(price.value);
                snsqa.setPostage(postage.value);
                snsqa.setAfterSale(afterSale.value);


                if (isEdit) {
                    updateSnsqa(snsqa, addPhotoURL);
                } else {
                    addSnsGoodsqa(snsqa);
                }

                break;
        }

    }

    @Override
    public void onTopLeftButtonClick(View v) {
        finish();
    }

    @Override
    public void onTopRightButtonClick(View v) {

    }

    @Override
    public void onPhotoClick(boolean isCheckedPhoto) {

    }

    @Override
    public void onDelete(SnsPhotoAdapter.ViewHolder holder) {
        if (photos.size() == 1) {
            showMsg("商品图片不能少于1张");
            return;
        }
        int position = holder.getAdapterPosition();
        if (isEdit) {
            snsqa.getPhotos().remove(position);
        } else {
            snsqa.getWxPhotoURL().remove(position);
        }
        adapter.delete(holder);

    }

    @Override
    public void onHttpSucceed(int what, SimpleResult ret, Response response) {
        super.onHttpSucceed(what, ret, response);

        if (ret != null) {
            if (what == URLManager.Add_Goods_qa.action) {
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    Snsqa snsqa = JSON.parseObject(ret.getData(), Snsqa.class);
                    setResult(snsqa);
                    showMsg("已成功生成商品，可在朋友圈问答中查看");
                } else {
                    showMsg(ret.getMessage());
                }
            } else if (what == URLManager.Update_Goods_qa.action) {
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    Snsqa snsqa = JSON.parseObject(ret.getData(), Snsqa.class);
                    setResult(snsqa);
                } else {
                    showMsg(ret.getMessage());
                }
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


    /**
     * 生成添加一个商品问答
     */
    private void addSnsGoodsqa(Snsqa qa) {
        SimpleRequest<SimpleResult> request = new SimpleRequest<>(URLManager.Add_Goods_qa.getURL(), URLManager.Add_Goods_qa.method, SimpleResult.class);
        request.setMultipartFormEnable();
        for (int i = 0; i < qa.getWxPhotoURL().size(); i++) {
            String path = qa.getWxPhotoURL().get(i);
            request.add(path, new BitmapBinary(ImageLoader.get(path), path, "image/png"));
        }
        request.add("name", qa.getName());
        request.add("price", qa.getPrice());
        request.add("postage", qa.getPostage());
        request.add("after_sale", qa.getAfterSale());
        request.add("description", qa.getDescribe());
        request.add("monent_id", qa.getSnsId());
        request.add("img_url_list", JSON.toJSONString(qa.getWxPhotoURL()));

        addTask2Queue(URLManager.Add_Goods_qa.action, request, true);
    }


    /**
     * 更新朋友圈商品问答
     *
     * @param qa
     */
    private void updateSnsqa(Snsqa qa, List<String> addPhotoURL) {

        SimpleRequest<SimpleResult> request = new SimpleRequest<>(URLManager.Update_Goods_qa.getURL(), URLManager.Update_Goods_qa.method, SimpleResult.class);
        request.setMultipartFormEnable();

        for (int i = 0; i < addPhotoURL.size(); i++) {
            String path = addPhotoURL.get(i);
            request.add(path, new BitmapBinary(ImageLoader.get(path), path, "image/png"));
            qa.getPhotos().add(new Snsqa.Photo(path, ""));
        }

        request.add("goods_id", qa.getId());
        request.add("name", qa.getName());
        request.add("price", qa.getPrice());
        request.add("postage", qa.getPostage());
        request.add("after_sale", qa.getAfterSale());
        request.add("description", qa.getDescribe());
        request.add("monent_id", qa.getSnsId());
        request.add("img_list", JSON.toJSONString(qa.getPhotos()));
        addTask2Queue(URLManager.Update_Goods_qa.action, request, true);
    }

    private void setResult(Snsqa qa) {
        Intent intent = getIntent();
        intent.putExtra(GenerateGoodsActivity.KEY_SNS_qa, qa);
        setResult(RESULT_OK, intent);
        finish();
    }


}
