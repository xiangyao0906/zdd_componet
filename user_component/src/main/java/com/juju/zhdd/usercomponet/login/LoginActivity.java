package com.juju.zhdd.usercomponet.login;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.juju.zhdd.usercomponet.BR;
import com.juju.zhdd.usercomponet.R;
import com.juju.zhdd.usercomponet.base.BaseMVVMActivity;
import com.juju.zhdd.usercomponet.databinding.LogInBinding;


/**
 * @author xiangyao
 */
public class LoginActivity extends BaseMVVMActivity<LogInBinding,LogInViewModel> {

    @Override
    public int initContentViewId(@Nullable Bundle savedInstanceState) {
        return R.layout.user_componet_activity_login;
    }



    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}