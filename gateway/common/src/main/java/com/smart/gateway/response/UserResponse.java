package com.smart.gateway.response;

import com.smart.gateway.entry.user.User;

public class UserResponse extends BaseResponse<UserResponse.Params> {

    public static class Params{
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
