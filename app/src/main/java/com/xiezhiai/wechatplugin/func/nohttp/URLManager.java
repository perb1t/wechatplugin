package com.xiezhiai.wechatplugin.func.nohttp;

import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by shijiwei on 2018/11/1.
 *
 * @Desc:
 */
public enum URLManager {

    Get_Captcha("/robot_bg/captcha", 100, RequestMethod.GET, "获取手机验证码"),
    Register("/robot_bg/register", 101, RequestMethod.PUT, "注册"),
    Login("/robot_bg/login", 102, RequestMethod.POST, "登录"),
    Logout("/robot_bg/logout", 103, RequestMethod.POST, "退出登录"),

    Bind_Wechat_User("/robot_bg/users/add_wx_user", 200, RequestMethod.POST, "绑定微信账号"),
    Get_Wechat_User_LIST("/robot_bg/users/get_wx_user_lst", 201, RequestMethod.GET, "获取绑定微信账号列表"),
    Delete_Wechat_User("/robot_bg/users/del_wx_user", 202, RequestMethod.POST, "删除绑定微信账号"),

    Update_Setting_Config("/robot_bg/users/set_wx_skills", 300, RequestMethod.POST, "更新配置"),
    Get_Setting_Config("/robot_bg/users/get_wx_skills", 301, RequestMethod.POST, "获取配置"),

    Add_Incoming_Record("/robot_bg/users/add_incoming", 400, RequestMethod.POST, "添加一笔红包、转账收益记录信息"),
    Get_Incoming_Record("/robot_bg/users/get_incoming_info", 401, RequestMethod.POST, "获取收益记录信息"),

    Add_Common_qa("/robot_bg/qa/question", 500, RequestMethod.PUT, "添加通用问答"),
    Get_Common_qa_List("/robot_bg/qa/question", 501, RequestMethod.GET, "获取通用问答列表"),
    Update_Common_qa("/robot_bg/qa/question", 502, RequestMethod.POST, "更新通用问答"),
    Delete_Common_qa("/robot_bg/qa/question", 503, RequestMethod.DELETE, "删除通用问答"),

    Add_Goods_qa("/robot_bg/qa/goods", 600, RequestMethod.PUT, "生成商品"),
    Get_Goods_qa_List("/robot_bg/qa/goods", 601, RequestMethod.GET, " 获取商品列表"),
    Update_Goods_qa("/robot_bg/qa/goods", 602, RequestMethod.POST, " 更新商品"),
    Delete_Goods_qa("/robot_bg/qa/goods", 603, RequestMethod.DELETE, " 删除商品"),

    Get_Text_qa_Answer("/robot_bg/qa/get_answer", 700, RequestMethod.POST, " 获取文字问题答案"),
    Get_Image_qa_Answer("/robot_bg/qa/get_img_answer", 701, RequestMethod.POST, " 获取图片问题答案"),

    Get_qa_Count("/robot_bg/qa/get_qa_count", 801, RequestMethod.POST, "获取知识库数量");

    String HOST = "https://trialchat.xiezhiai.com";
    String PORT = "6160";
    public String path;
    public String desc;
    public int action;
    public RequestMethod method;

    URLManager(String path, int action, RequestMethod method, String desc) {
        this.path = path;
        this.desc = desc;
        this.action = action;
        this.method = method;
    }

    public String getURL() {
        return HOST + ":" + PORT + path;
    }
}
