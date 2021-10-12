package com.example.customnewdemo.app.login

/**
 * 登录状态管理
 */
class LoginContext {

    var mState: UserState = UserLogoutState() // 默认未登录状态

    companion object {

        val TAG = "LoginContext"

        val INSTANCE = LoginContext()

        public fun getInstance(): LoginContext {
            return INSTANCE
        }
    }

    /**
     * set user login state
     *
     */
    private fun setUserState(state: UserState) {
        this.mState = state
    }


    /**
     *  login
     */
    public fun login() {
        setUserState(UserLoginInState())
    }

    /**
     * loginout
     */
    public fun loginOut() {
        setUserState(UserLogoutState())
    }


}